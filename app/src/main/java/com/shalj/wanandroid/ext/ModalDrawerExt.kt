package com.shalj.wanandroid.ext

import androidx.compose.material3.DrawerState

suspend fun DrawerState.toggle() {
    if (isOpen) { close() } else { open() }
}