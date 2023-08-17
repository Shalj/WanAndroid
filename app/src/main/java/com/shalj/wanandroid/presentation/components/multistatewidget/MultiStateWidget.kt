package com.shalj.wanandroid.presentation.components.multistatewidget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun MultiStateWidget(
    modifier: Modifier = Modifier,
    providerState: (prevState: MultiStateWidgetState) -> MultiStateWidgetState = { MultiStateWidgetState.Content },
    loading: @Composable () -> Unit = { MultiStateWidgetDefault.LoadingWidget() },
    error: @Composable () -> Unit = { MultiStateWidgetDefault.ErrorWidget() },
    empty: @Composable () -> Unit = { MultiStateWidgetDefault.EmptyWidget() },
    content: @Composable () -> Unit
) {
    var state by rememberSaveable {
        mutableStateOf<MultiStateWidgetState>(MultiStateWidgetState.Content)
    }
    state = providerState(state)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (state) {
            MultiStateWidgetState.Loading -> loading()
            MultiStateWidgetState.Error -> error()
            MultiStateWidgetState.Empty -> empty()
            MultiStateWidgetState.Content -> content()
        }
    }
}

object MultiStateWidgetDefault {
    @Composable
    fun LoadingWidget() {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round
            )
        }
    }

    @Composable
    fun EmptyWidget() {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text(text = "Noting to show")
        }
    }

    @Composable
    fun ErrorWidget(reload: () -> Unit = {}) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Error")
            OutlinedButton(modifier = Modifier.padding(top = 8.dp), onClick = reload) {
                Text(text = "Retry")
            }
        }
    }
}