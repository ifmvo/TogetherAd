package com.ifmvo.togetherad.core.other

import android.util.Log

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
internal fun Any.logd(msg: String?) {
    Log.d("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun Any.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}