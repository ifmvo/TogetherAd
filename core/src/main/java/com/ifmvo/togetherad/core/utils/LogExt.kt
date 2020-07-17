package com.ifmvo.togetherad.core.utils

import android.util.Log
import com.ifmvo.togetherad.core.TogetherAd

/* 
 * Created by Matthew_Chen on 2018/12/24.
 */
fun String.logv(tag: String? = "TogetherAd") {
    if (TogetherAd.printLogEnable)
        Log.v(tag, this)
}

fun String.logd(tag: String? = "TogetherAd") {
    if (TogetherAd.printLogEnable)
        Log.d(tag, this)
}

fun String.logi(tag: String? = "TogetherAd") {
    if (TogetherAd.printLogEnable)
        Log.i(tag, this)
}

fun String.logw(tag: String? = "TogetherAd") {
    if (TogetherAd.printLogEnable)
        Log.w(tag, this)
}

fun String.loge(tag: String? = "TogetherAd") {
    if (TogetherAd.printLogEnable)
        Log.e(tag, this)
}