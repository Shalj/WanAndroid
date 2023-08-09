package com.shalj.wanandroid.ui.screen.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shalj.wanandroid.ui.screen.home.HomeScreen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController, mainViewModel: MainViewModel = hiltViewModel()) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background.copy(alpha = .8f)) {
        HorizontalPager(
            state = rememberPagerState { 1 },
            modifier = Modifier.padding(it)
        ) {
            HomeScreen(navController)
        }
    }
}