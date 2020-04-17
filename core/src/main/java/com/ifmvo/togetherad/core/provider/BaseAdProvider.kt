package com.ifmvo.togetherad.core.provider

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseAdProvider : IAdProvider {

    fun callbackStartRequest(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 开始请求".logi()
        listener.onAdStartRequest(adProviderType)
    }

    fun callbackLoaded(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 请求成功了".logi()
        listener.onAdLoaded(adProviderType)
    }

    fun callbackClicked(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 点击了".logi()
        listener.onAdClicked(adProviderType)
    }

    fun callbackExposure(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 曝光了".logi()
        listener.onAdExposure(adProviderType)
    }

    fun callbackFailed(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener, failedMsg: String?) {
        "${adProviderType}: 请求失败了：$failedMsg".loge()
        listener.onAdFailed(adProviderType, failedMsg)
    }

    fun callbackDismiss(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 消失了".logi()
        listener.onAdDismissed(adProviderType)
    }

}