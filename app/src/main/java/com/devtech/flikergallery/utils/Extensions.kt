package com.devtech.flikergallery.utils

import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

fun EditText.getQueryTextChangeStateFlow(): StateFlow<String> {
    val query = MutableStateFlow("")

    addTextChangedListener {
        query.value = it?.toString() ?: ""
    }
    return query
}