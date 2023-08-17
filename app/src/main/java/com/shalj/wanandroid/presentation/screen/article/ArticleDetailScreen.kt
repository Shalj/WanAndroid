package com.shalj.wanandroid.presentation.screen.article

import android.annotation.SuppressLint
import android.content.Intent
import android.webkit.WebView
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ExitToApp
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material.icons.rounded.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.web.LoadingState
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberSaveableWebViewState
import com.shalj.wanandroid.presentation.components.AnimatedHeart
import com.shalj.wanandroid.presentation.components.WanSpacer
import com.shalj.wanandroid.presentation.components.WanTopAppBar
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import com.shalj.wanandroid.utils.logE

@Preview
@Composable
fun ArticleDetailPreview() {
    WanAndroidTheme {
        ArticleDetailScreen(-1, login = {}, navigateUp = {

        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun ArticleDetailScreen(
    id: Int?,
    viewModel: ArticleDetailVM = hiltViewModel(),
    navigateUp: () -> Unit,
    login: () -> Unit
) {

    LaunchedEffect(key1 = id) {
        viewModel.onEvent(ArticleDetailEvent.Init(id))
    }


    val state by viewModel.state.collectAsStateWithLifecycle(initialValue = ArticleDetailState())
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val webviewState = rememberSaveableWebViewState()
    var progress by remember {
        mutableFloatStateOf(0f)
    }

    var webview by remember {
        mutableStateOf<WebView?>(null)
    }

    LaunchedEffect(key1 = webviewState.loadingState, block = {
        if (webviewState.loadingState is LoadingState.Loading) {
            progress = (webviewState.loadingState as LoadingState.Loading).progress
        }
    })

    LaunchedEffect(key1 = state.needLogin) {
        if (state.needLogin) {
            login()
        }
    }

    LaunchedEffect(key1 = state.showMoreMenu) {
        if (state.showMoreMenu) {
            bottomSheetState.show()
        } else {
            bottomSheetState.hide()
        }
    }


    Scaffold(
        topBar = {
            WanTopAppBar(
                title = state.article.title.orEmpty(),
                onBackPressed = navigateUp,
                actions = {
                    AnimatedHeart(
                        modifier = Modifier.size(30.dp),
                        isUpdating = state.isUpdatingLikeState,
                        selected = state.article.collect ?: false,
                        onToggle = { viewModel.onEvent(ArticleDetailEvent.CollectArticle(state.article)) }
                    )
                    IconButton(
                        onClick = {
                            viewModel.onEvent(ArticleDetailEvent.ShowMoreMenu)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "more",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        if (state.article.link.orEmpty().isNotEmpty())
            Box(
                modifier = Modifier.padding(paddingValues)
            ) {
                WebView(
                    modifier = Modifier.fillMaxSize(),
                    state = webviewState,
                    onCreated = {
                        webview = it
                        it.settings.apply {
                            javaScriptEnabled = true
                            databaseEnabled = true
                            domStorageEnabled = true
                            allowFileAccess = true
                            allowContentAccess = true
                            javaScriptCanOpenWindowsAutomatically = true
                            builtInZoomControls = true
                            displayZoomControls = false
                            useWideViewPort = true
                            loadWithOverviewMode = true
                            supportZoom()
                        }
                        it.loadUrl(state.article.link.orEmpty())
                    }
                )
                AnimatedVisibility(
                    visible = progress < 1,
                    enter = slideIn(animationSpec = tween(200), initialOffset = { size ->
                        IntOffset(0, size.height)
                    }),
                    exit = slideOut(animationSpec = tween(200), targetOffset = { size ->
                        IntOffset(0, -size.height)
                    })
                ) {
                    LinearProgressIndicator(
                        modifier = Modifier.fillMaxWidth(),
                        progress = progress,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.primaryContainer,
                        strokeCap = StrokeCap.Butt
                    )
                }
            }

        BottomMenu(state, viewModel, bottomSheetState, webview)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BottomMenu(
    state: ArticleDetailState,
    viewModel: ArticleDetailVM,
    bottomSheetState: SheetState,
    webView: WebView?,
) {
    val context = LocalContext.current
    val urlHandler = LocalUriHandler.current
    if (state.showMoreMenu)
        ModalBottomSheet(
            onDismissRequest = { viewModel.onEvent(ArticleDetailEvent.HideMoreMenu) },
            shape = RoundedCornerShape(topStart = 10.dp, topEnd = 10.dp),
            sheetState = bottomSheetState
        ) {
            Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)) {
                BottomMenuItem(Icons.Rounded.Share, "分享") {
                    val sendIntent: Intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, state.article.link.orEmpty())
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, null)
                    context.startActivity(shareIntent)
                    viewModel.onEvent(ArticleDetailEvent.HideMoreMenu)
                }
                WanSpacer(width = 10.dp)
                BottomMenuItem(Icons.Rounded.Refresh, "刷新") {

                    logE("webview", webView)
                    webView?.reload()
                    viewModel.onEvent(ArticleDetailEvent.HideMoreMenu)
                }
                WanSpacer(width = 10.dp)
                BottomMenuItem(Icons.Rounded.ExitToApp, "浏览器打开") {
                    urlHandler.openUri(state.article.link.orEmpty())
                    viewModel.onEvent(ArticleDetailEvent.HideMoreMenu)
                }
            }
        }

}

@Composable
private fun RowScope.BottomMenuItem(imageVector: ImageVector, title: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() }
            .padding(vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            modifier = Modifier.size(30.dp),
            imageVector = imageVector,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
        WanSpacer(height = 8.dp)
        Text(text = title, color = MaterialTheme.colorScheme.primary, fontSize = 14.sp)
    }
}