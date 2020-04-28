package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull

/* 
 * (●ﾟωﾟ●) 通用的监听器
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface SplashListener : BaseListener {

    fun onAdStartRequest(@NonNull providerType: String) {}

    fun onAdLoaded(@NonNull providerType: String) {}

    fun onAdClicked(@NonNull providerType: String) {}

    fun onAdExposure(@NonNull providerType: String) {}

    fun onAdFailed(@NonNull providerType: String, failedMsg: String?) {}

    fun onAdFailedAll(@NonNull failedMsg: String?) {}

    fun onAdDismissed(@NonNull providerType: String) {}

}