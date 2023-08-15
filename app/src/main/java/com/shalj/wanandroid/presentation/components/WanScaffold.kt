package com.shalj.wanandroid.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun WanScaffold(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = { WanTopAppBar() },
    containerColor: Color = MaterialTheme.colorScheme.background,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarHost: @Composable (() -> Unit)? = null,
    snackbarAction: @Composable (() -> Unit)? = null,
    loading: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        containerColor = containerColor,
        topBar = topBar,
        snackbarHost = snackbarHost ?: {
            WanSnackBar(
                modifier = Modifier
                    .padding(bottom = 40.dp)
                    .padding(horizontal = 32.dp),
                snackbarHostState = snackbarHostState,
                action = snackbarAction ?: {
                    TextButton(onClick = { snackbarHostState.currentSnackbarData?.dismiss() }) {
                        Text(text = "关闭")
                    }
                },
            )
        }
    ) {
        Box(modifier = modifier.padding(it)) {
            content()
            loading()
        }
    }
}