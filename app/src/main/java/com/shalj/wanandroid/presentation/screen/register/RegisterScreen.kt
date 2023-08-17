package com.shalj.wanandroid.presentation.screen.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
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
import coil.compose.rememberAsyncImagePainter
import com.shalj.wanandroid.R
import com.shalj.wanandroid.presentation.components.PasswordTextField
import com.shalj.wanandroid.presentation.components.WanLoading
import com.shalj.wanandroid.presentation.components.WanSpacer
import com.shalj.wanandroid.presentation.components.WanTextField
import com.shalj.wanandroid.presentation.components.WanTopAppBar
import com.shalj.wanandroid.ui.theme.WanAndroidTheme
import kotlinx.coroutines.launch

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun RegisterScreenPreview() {
    WanAndroidTheme {
        RegisterScreen({})
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RegisterScreen(
    onBackPressed: () -> Unit,
    viewModel: RegisterIntent = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle(RegisterState())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val keyboardController = LocalSoftwareKeyboardController.current

    SideEffect {
        scope.launch {
            if (state.errorMsg.isNotEmpty()) snackbarHostState.showSnackbar(state.errorMsg)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            WanTopAppBar(onBackPressed = onBackPressed)
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
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
                        viewModel.onEvent(RegisterEvent.InputUsername(username))
                    }
                )
                WanSpacer(15.dp)
                //密码输入框
                PasswordTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    state.password,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChanged = { password ->
                        viewModel.onEvent(RegisterEvent.InputPassword(password))
                    }
                )
                WanSpacer(15.dp)
                //确认密码输入框
                PasswordTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    state.rePassword,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Password
                    ),
                    onValueChanged = { password ->
                        viewModel.onEvent(RegisterEvent.InputRePassword(password))
                    }
                )
                WanSpacer(15.dp)
                //验证码输入框
                WanTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    value = state.verifyCode,
                    label = "验证码(选填)",
                    placeholder = "请输入验证码(选填)",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Face,
                            contentDescription = "verifyCode",
                            tint = MaterialTheme.colorScheme.primary,
                        )
                    },
                    keyboardActions = KeyboardActions(onDone = {
                        keyboardController?.hide()
                        viewModel.onEvent(RegisterEvent.Register)
                    }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done,
                        keyboardType = KeyboardType.Number,
                    ),
                    onValueChanged = { verifyCode ->
                        viewModel.onEvent(RegisterEvent.InputVerifyCode(verifyCode))
                    }
                )

                WanSpacer(40.dp)

                //注册按钮
                TextButton(
                    modifier = Modifier.width(200.dp),
                    onClick = {
                        keyboardController?.hide()
                        viewModel.onEvent(RegisterEvent.Register, onBackPressed)
                    },
                    enabled = state.registerBtnEnabled,
                    colors = ButtonDefaults.textButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                ) {
                    Text(text = "注册")
                }
                WanSpacer(40.dp)
                Image(
                    modifier = Modifier.size(100.dp),
                    painter = rememberAsyncImagePainter(model = "https://www.wanandroid.com/resources/image/pc/weixin_hongyang.jpg"),
                    contentDescription = "qrCode"
                )
                WanSpacer(10.dp)
                Text(text = buildAnnotatedString {
                    append("扫码关注，发送「")
                    withStyle(SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                        append("我爱安卓")
                    }
                    append("」获取验证码")
                })
            }

            WanLoading(state.showLoading, "注册中…", onDismissRequest = {
                viewModel.onEvent(RegisterEvent.DismissLoading)
            })
        }
    )
}