package com.shalj.wanandroid.presentation.screen.article

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.IntOffset
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import com.shalj.wanandroid.presentation.components.WanTopAppBar

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(link: String = "", title: String = "", back: () -> Unit) {
    val webviewState = rememberWebViewState(url = link)
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    LaunchedEffect(key1 = webviewState.loadingState, block = {
        if (webviewState.loadingState is LoadingState.Loading) {
            progress = (webviewState.loadingState as LoadingState.Loading).progress
        }
    })

    Scaffold(
        topBar = { WanTopAppBar(title = title, onBackPressed = back) }
    ) { paddingValues ->
        Box(
            modifier = Modifier.padding(paddingValues)
        ) {
            WebView(
                modifier = Modifier.fillMaxSize(),
                state = webviewState,
                onCreated = { webview ->
                    webview.settings.apply {
                        javaScriptEnabled = true
                        databaseEnabled = true
                    }
                }
            )
            AnimatedVisibility(
                visible = progress < 1,
                enter = slideIn(animationSpec = tween(200), initialOffset = { size ->
                    IntOffset(0, size.height)
                }),
                exit = slideOut(animationSpec = tween(200), targetOffset = { size ->
                    IntOffset(0, -size.height)
                })
            ) {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = progress,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer,
                    strokeCap = StrokeCap.Butt
                )
            }
        }
    }
}