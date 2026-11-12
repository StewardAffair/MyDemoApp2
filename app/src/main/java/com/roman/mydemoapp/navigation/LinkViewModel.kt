package com.roman.mydemoapp.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Holds a playlist link received via share/deep link until navigation consumes it. */
@HiltViewModel
class LinkViewModel @Inject constructor() : ViewModel() {
    private val _incomingLink = MutableStateFlow<String?>(null)
    val incomingLink: StateFlow<String?> = _incomingLink.asStateFlow()

    fun onNewLink(link: String?) {
        if (link != null) _incomingLink.value = link
    }

    fun onLinkHandled() {
        _incomingLink.value = null
    }
}
