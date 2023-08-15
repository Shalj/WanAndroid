package com.shalj.wanandroid.presentation.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shalj.wanandroid.R
import com.shalj.wanandroid.presentation.components.PasswordTextField
import com.shalj.wanandroid.presentation.components.WanLoading
import com.shalj.wanandroid.presentation.components.WanScaffold
import com.shalj.wanandroid.presentation.components.WanSpacer
import com.shalj.wanandroid.presentation.components.WanTextField
import com.shalj.wanandroid.presentation.components.WanTopAppBar
import com.shalj.wanandroid.presentation.theme.WanAndroidTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    WanAndroidTheme {
        LoginScreen({}, {})
    }
}

@Composable
fun LoginScreen(
    register: () -> Unit, onBackPressed: () -> Unit, viewModel: LoginIntent = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle(LoginState())
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember {
        SnackbarHostState()
    }
    val scope = rememberCoroutineScope()

    if (state.loginSuccess) {
        SideEffect {
            scope.launch {
                snackbarHostState.showSnackbar("登录成功")
            }
            onBackPressed()
        }
    }

    WanScaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarAction = {
            TextButton(onClick = onBackPressed) { Text(text = "关闭") }
        },
        snackbarHostState = snackbarHostState,
        loading = {
            WanLoading(state.showLoading, "登录中…", onDismissRequest = {
                viewModel.onEvent(LoginEvent.DismissLoading)
            })
        },
        topBar = {
            WanTopAppBar(navigationIcon = {
                IconButton(modifier = Modifier.padding(start = 10.dp),
                    onClick = { onBackPressed() },
                    content = {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = "onBackPressed",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    })
            })
        },
        content = {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                //icon
                Image(
                    modifier = Modifier.size(80.dp),
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "",
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
                )
                WanSpacer(15.dp)
                //用户名输入框
                WanTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    value = state.username,
                    label = "用户名",
                    placeholder = "请输入用户名",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Person,
                            contentDescription = "username",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password,
                    ),
                    onValueChanged = { username ->
                        viewModel.onEvent(LoginEvent.InputUsername(username))
                    }
                )
                WanSpacer(15.dp)
                //密码输入框
                PasswordTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    state.password,
                    keyboardActions = KeyboardActions(onGo = { viewModel.onEvent(LoginEvent.Login) }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Go,
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChanged = { password ->
                        viewModel.onEvent(LoginEvent.InputPassword(password))
                    }
                )

                WanSpacer(40.dp)

                //登录按钮
                TextButton(
                    modifier = Modifier.width(200.dp),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(LoginEvent.Login)
                    },
                    enabled = state.loginBtnEnabled,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Text(text = "登录")
                }
                WanSpacer(15.dp)
                //注册提示及按钮
                Text(
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .clickable { register() }, text = buildAnnotatedString {
                        append("还没账号？")
                        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                            append("点击注册")
                        }
                    }
                )
            }
        })
}