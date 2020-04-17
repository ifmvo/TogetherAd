package com.ifmvo.togetherad.core.listener

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

/* 
 * (●ﾟωﾟ●) 通用的监听器
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface SplashListener {

    fun onAdStartRequest(@NonNull providerType: AdProviderType) {}

    fun onAdLoaded(@NonNull providerType: AdProviderType) {}

    fun onAdClicked(@NonNull providerType: AdProviderType) {}

    fun onAdExposure(@NonNull providerType: AdProviderType) {}

    fun onAdFailed(@NonNull providerType: AdProviderType, failedMsg: String?) {}

    fun onAdFailedAll(@NonNull failedMsg: String?) {}

    fun onAdDismissed(@NonNull providerType: AdProviderType) {}

}