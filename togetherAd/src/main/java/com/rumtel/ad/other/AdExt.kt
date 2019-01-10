package com.rumtel.ad.other

import android.util.Log
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieBase

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
internal fun AdBase.logd(msg: String?) {
    Log.d("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdBase.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdRandomUtil.logd(msg: String?) {
    Log.d("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdRandomUtil.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdViewPreMovieBase.logd(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdViewPreMovieBase.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}