package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.listener.CommonListener

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelper : BaseHelper() {

    fun showSplashAd(@NonNull activity: Activity, @NonNull alias: String, radio: String? = null, @NonNull container: ViewGroup, @NonNull listener: CommonListener) {
        val adProvider = randomProvider(radio) ?: throw Exception("随机到的广告商没注册")
        adProvider.showSplashAd(activity, alias, radio, container, listener)
    }
}