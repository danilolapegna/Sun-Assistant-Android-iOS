package com.sunassistant.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun SunAssistantWebView(url: String) {
    val isPageLoaded = remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!isPageLoaded.value) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }

        AndroidView(
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.apply {
                        javaScriptEnabled = true
                        useWideViewPort = true
                        builtInZoomControls = false
                        displayZoomControls = false
                        setSupportZoom(false)
                    }

                    webViewClient = object : WebViewClient() {
                        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                            isPageLoaded.value = false
                        }

                        override fun onPageFinished(view: WebView?, url: String?) {
                            isPageLoaded.value = true
                        }
                    }
                    loadUrl(url)
                }
            }
        )
    }
}