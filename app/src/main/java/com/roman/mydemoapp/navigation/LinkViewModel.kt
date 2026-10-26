package com.roman.mydemoapp.navigation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Holds a playlist link received via share/deep link until navigation consumes it. */
class LinkViewModel : ViewModel() {
    private val _incomingLink = MutableStateFlow<String?>(null)
    val incomingLink: StateFlow<String?> = _incomingLink.asStateFlow()

    fun onNewLink(link: String?) {
        if (link != null) _incomingLink.value = link
    }

    fun onLinkHandled() {
        _incomingLink.value = null
    }
}
