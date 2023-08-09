package com.shalj.wanandroid.ui.screen.start

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.compose.rememberNavController
import com.shalj.wanandroid.R
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.route.AppRoute
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    WanAndroidTheme {
        StartScreen(appNavController = rememberNavController())
    }
}

@Composable
fun setupLifeCycle(
    lifecycleOwner: LifecycleOwner,
    viewModel: BaseViewModel,
) {
    val currentOnStart by rememberUpdatedState(viewModel::onStart)
    val currentOnStop by rememberUpdatedState(viewModel::onStop)
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> currentOnStart()
                Lifecycle.Event.ON_STOP -> currentOnStop()
                else -> {}
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun StartScreen(
    appNavController: NavController,
    startViewModel: StartViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val uiState by startViewModel.startUIState.collectAsStateWithLifecycle()
    setupLifeCycle(lifecycleOwner, startViewModel)

    LaunchedEffect(Unit) {
        delay(2500)
        val options = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .build()
        appNavController.navigate(AppRoute.mainScreen, options)
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = uiState.iconVisible,
            enter = expandVertically(expandFrom = Alignment.Top) + fadeIn(initialAlpha = 0.2f)
        ) {
            Icon(
                modifier = Modifier.size(120.dp),
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "startIcon",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        AnimatedVisibility(
            modifier = Modifier.padding(top = 20.dp),
            visible = uiState.textVisible,
            enter = expandVertically(expandFrom = Alignment.Bottom) + fadeIn(initialAlpha = 0.1f),
        ) {
            Text(
                text = "玩安卓",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}