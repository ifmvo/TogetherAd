package com.rumtel.ad.other

import android.util.Log
import com.rumtel.ad.AdRandomUtil
import com.rumtel.ad.helper.AdHelperBase

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
internal fun AdHelperBase.logd(msg: String?) {
    Log.d("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdHelperBase.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdRandomUtil.logd(msg: String?) {
    Log.d("TogetherAd", "${this.javaClass.simpleName}: $msg")
}

internal fun AdRandomUtil.loge(msg: String?) {
    Log.e("TogetherAd", "${this.javaClass.simpleName}: $msg")
}