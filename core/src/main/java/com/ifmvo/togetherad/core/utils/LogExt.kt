package com.ifmvo.togetherad.core.utils

import android.util.Log

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
fun String.logv(tag: String? = "TogetherAd") {
    Log.v(tag, this)
}

fun String.logd(tag: String? = "TogetherAd") {
    Log.d(tag, this)
}

fun String.logi(tag: String? = "TogetherAd") {
    Log.i(tag, this)
}

fun String.logw(tag: String? = "TogetherAd") {
    Log.w(tag, this)
}

fun String.loge(tag: String? = "TogetherAd") {
    Log.e(tag, this)
}