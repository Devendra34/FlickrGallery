package com.devtech.flikergallery.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.devtech.flikergallery.databinding.ActivityMainBinding
import com.devtech.flikergallery.repository.PhotosRepository
import com.devtech.flikergallery.ui.adapters.PagingLoadStateAdapter
import com.devtech.flikergallery.ui.adapters.PhotoAdapter
import com.devtech.flikergallery.utils.getQueryTextChangeStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PhotosViewModel
    private val adapter = PhotoAdapter()
    private var searchJob: Job? = null
    companion object {
        private const val TAG = "my_test"
        private const val SAVED_QUERY = "saved_query"
    }

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val savedQuery = savedInstanceState?.getString(SAVED_QUERY) ?: ""
        setUpUI(savedQuery)
        setUpViewModel()
        setUpObservers()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val lastQuery = binding.searchBar.editText?.text?.toString() ?: ""
        outState.putString(SAVED_QUERY, lastQuery)
    }

    private fun setUpUI(savedQuery: String) {
        binding.rv.adapter = adapter.withLoadStateHeaderAndFooter(
            header = PagingLoadStateAdapter(adapter::retry),
            footer = PagingLoadStateAdapter(adapter::retry)
        )
        binding.searchBar.editText!!.setText(savedQuery)
    }

    private fun setUpViewModel() {
        val repository = PhotosRepository()
        val viewModelProviderFactory = PhotosViewModelProviderFactory(repository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(PhotosViewModel::class.java)
    }

    @FlowPreview
    private fun setUpObservers() {

        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect {
                    binding.rv.scrollToPosition(0)
                }
        }
        lifecycleScope.launch {
            binding.searchBar.editText!!.getQueryTextChangeStateFlow()
                .debounce(300)
                .distinctUntilChanged()
                .flowOn(Dispatchers.Default)
                .collect {
                    search(it)
                }
        }

    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchPhoto(query).catch {
                Log.d(TAG, "search error: $it")
            }.collectLatest {
                adapter.submitData(it)
            }
        }
    }
}