package com.shalj.wanandroid.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import com.shalj.wanandroid.R

@Composable
fun rememberImagePainter(model: Any?) =
    rememberAsyncImagePainter(
        model = model,
//        placeholder = painterResource(id = R.drawable.image_placeholder),
        contentScale = ContentScale.Crop
    )