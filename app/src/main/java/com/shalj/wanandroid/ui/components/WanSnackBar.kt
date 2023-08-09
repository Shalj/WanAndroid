package com.shalj.wanandroid.ui.components

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

@Composable
fun WanSnackBar(
    modifier: Modifier = Modifier,
    textColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    containerColor: Color = MaterialTheme.colorScheme.primaryContainer,
    shape: Shape = CircleShape,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    action: @Composable (() -> Unit)? = null,
) {
    SnackbarHost(snackbarHostState) { data ->
        Snackbar(
            modifier = modifier,
            shape = shape,
            containerColor = containerColor,
            action = action
        ) {
            Text(
                text = data.visuals.message,
                color = textColor
            )
        }
    }
}