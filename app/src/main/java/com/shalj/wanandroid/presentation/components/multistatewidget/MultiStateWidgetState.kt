package com.shalj.wanandroid.presentation.components.multistatewidget

sealed class MultiStateWidgetState {
    object Loading : MultiStateWidgetState()
    object Empty : MultiStateWidgetState()
    object Content : MultiStateWidgetState()
    object Error : MultiStateWidgetState()
}