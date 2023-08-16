package com.shalj.wanandroid.presentation.screen.start

import androidx.compose.animation.AnimatedVisibility
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
import com.shalj.wanandroid.R
import com.shalj.wanandroid.base.BaseViewModel
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.delay

@Preview(showBackground = true)
@Composable
fun StartPreview() {
    WanAndroidTheme {
        StartScreen {}
    }
}

@Composable
fun SetupLifeCycle(
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

@Composable
fun StartScreen(
    startViewModel: StartViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
    navigateToMainScreen: () -> Unit,
) {
    val uiState by startViewModel.startUIState.collectAsStateWithLifecycle()
    SetupLifeCycle(lifecycleOwner, startViewModel)

    LaunchedEffect(Unit) {
        delay(2500)
        navigateToMainScreen()
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
                text = "坚持下去，你就是下一个大牛！",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}