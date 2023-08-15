package com.shalj.wanandroid.presentation.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp

/**
 * @param title 标题，当 <code>[titleWidget] 为 null 时生效
 * @param titleWidget  标题组件，如果不为 null 则 <code>[title] 不生效
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WanTopAppBar(
    title: String = "",
    titleWidget: @Composable (() -> Unit)? = null,
    navigationIcon: @Composable (() -> Unit)? = null,
    actions: @Composable RowScope.()->Unit = {},
    onBackPressed: () -> Unit = { }
) {
    TopAppBar(
        title = titleWidget ?: {
            Text(
                text = title,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        },
        navigationIcon = navigationIcon ?: {
            IconButton(onClick = onBackPressed) {
                Icon(Icons.Filled.ArrowBack, "backIcon")
            }
        },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.background,
        ),
        actions = actions
    )
}