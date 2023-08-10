package com.shalj.wanandroid.ui.screen.main.project

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun ProjectScreen() {
    Text(modifier = Modifier.fillMaxSize(), text = "project", textAlign = TextAlign.Center)
}