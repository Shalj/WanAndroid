package com.shalj.wanandroid.ui.components.multistatewidget

import androidx.compose.runtime.Composable

sealed class MultiStateWidgetState {
    object Loading : MultiStateWidgetState()
    object Empty : MultiStateWidgetState()
    object Content : MultiStateWidgetState()
    object Error : MultiStateWidgetState()
}