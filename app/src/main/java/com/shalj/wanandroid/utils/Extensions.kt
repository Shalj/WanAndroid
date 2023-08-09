package com.shalj.wanandroid.utils

import com.google.gson.Gson

fun Any.toJson() = Gson().toJson(this)