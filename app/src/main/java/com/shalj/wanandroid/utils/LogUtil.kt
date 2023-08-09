package com.shalj.wanandroid.utils

import android.util.Log

//val isDebug = BuildConfig.DEBUG
val isDebug = true

fun logE(tag: String, msg: Any?) {
    if (isDebug)
        Log.e(tag, msg.toString())
}

fun logD(tag: String, msg: String) {
    if (isDebug)
        Log.d(tag, msg)
}

fun logW(tag: String, msg: String) {
    if (isDebug)
        Log.w(tag, msg)
}