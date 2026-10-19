package com.roman.mydemoapp.navigation

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class LinkViewModelTest {
    @Test
    fun newLinkIsExposedUntilHandled() {
        val viewModel = LinkViewModel()
        assertNull(viewModel.incomingLink.value)
        viewModel.onNewLink("https://example.com/p")
        assertEquals("https://example.com/p", viewModel.incomingLink.value)
        viewModel.onLinkHandled()
        assertNull(viewModel.incomingLink.value)
    }

    @Test
    fun nullLinkDoesNotOverwritePendingLink() {
        val viewModel = LinkViewModel()
        viewModel.onNewLink("https://example.com/p")
        viewModel.onNewLink(null)
        assertEquals("https://example.com/p", viewModel.incomingLink.value)
    }
}
