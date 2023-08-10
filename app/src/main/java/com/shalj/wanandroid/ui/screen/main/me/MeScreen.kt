package com.shalj.wanandroid.ui.screen.main.me

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shalj.wanandroid.R
import com.shalj.wanandroid.ui.components.WanTopAppBar
import com.shalj.wanandroid.ui.theme.WanAndroidTheme


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MeScreenPreview() {
    WanAndroidTheme {
        MeScreen {}
    }
}

@Composable
fun MeScreen(
    login: () -> Unit = {},
    goMessage: () -> Unit = {},
    goCollect: () -> Unit = {},
    goShare: () -> Unit = {},
    goSetting: () -> Unit = {},
    goTools: () -> Unit = {},
    onBackPressed: () -> Unit,
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            WanTopAppBar(navigationIcon = {
                IconButton(
                    modifier = Modifier.padding(start = 10.dp),
                    onClick = { onBackPressed() },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "onBackPressed",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                )
            })
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                MeInfo()
                SettingContent()
            }
        }
    )
}

@Composable
private fun MeInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            modifier = Modifier.size(66.dp),
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Shalj", fontSize = 24.sp)
            Text(text = "ID:", fontSize = 14.sp)
            Text(text = "积分：等级：排名", fontSize = 14.sp)
        }
    }
}

@Composable
private fun ColumnScope.SettingContent(
    login: () -> Unit = {},
    goMessage: () -> Unit = {},
    goCollect: () -> Unit = {},
    goShare: () -> Unit = {},
    goTools: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .background(
                MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp)
            )
            .padding(top = 15.dp)
    ) {
        var isLogin by remember {
            mutableStateOf(false)
        }
        MeSettingItem(
            icon = R.drawable.round_message_24,
            title = "消息中心",
            onClick = if (isLogin) goMessage else login
        )
        MeSettingItem(
            icon = R.drawable.round_share_24,
            title = "我的分享",
            onClick = if (isLogin) goShare else login
        )
        MeSettingItem(
            icon = R.drawable.round_favorite_24,
            title = "我的收藏",
            onClick = if (isLogin) goCollect else login
        )
        MeSettingItem(icon = R.drawable.round_build_24, title = "常用工具", onClick = goTools)
    }
}

@Composable
fun MeSettingItem(icon: Int, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = icon), contentDescription = title,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 10.dp),
            text = title,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Icon(
            painter = painterResource(id = R.drawable.round_navigate_next_24),
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = .6f)
        )
    }
}