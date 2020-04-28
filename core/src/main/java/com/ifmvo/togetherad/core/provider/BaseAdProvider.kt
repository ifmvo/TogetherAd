package com.ifmvo.togetherad.core.provider

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi

/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseAdProvider : IAdProvider {

    /**
     * --------------------------- 开屏 ---------------------------
     */
    fun callbackSplashStartRequest(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 开始请求".logi()
        listener.onAdStartRequest(adProviderType)
    }

    fun callbackSplashLoaded(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 请求成功了".logi()
        listener.onAdLoaded(adProviderType)
    }

    fun callbackSplashClicked(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 点击了".logi()
        listener.onAdClicked(adProviderType)
    }

    fun callbackSplashExposure(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 曝光了".logi()
        listener.onAdExposure(adProviderType)
    }

    fun callbackSplashFailed(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener, failedMsg: String?) {
        "${adProviderType}: 请求失败了：$failedMsg".loge()
        listener.onAdFailed(adProviderType, failedMsg)
    }

    fun callbackSplashDismiss(@NonNull adProviderType: AdProviderType, @NonNull listener: SplashListener) {
        "${adProviderType}: 消失了".logi()
        listener.onAdDismissed(adProviderType)
    }

    /**
     * --------------------------- 原生信息流 ---------------------------
     */
    fun callbackFlowStartRequest(@NonNull adProviderType: AdProviderType, @NonNull listener: NativeListener) {
        "${adProviderType}: 开始请求".logi()
        listener.onAdStartRequest(adProviderType)
    }

    fun callbackFlowLoaded(@NonNull adProviderType: AdProviderType, @NonNull listener: NativeListener, @NonNull adList: List<Any>) {
        "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
        listener.onAdLoaded(adProviderType, adList)
    }

    fun callbackFlowFailed(@NonNull adProviderType: AdProviderType, @NonNull listener: NativeListener, failedMsg: String?) {
        "${adProviderType}: 请求失败了：$failedMsg".loge()
        listener.onAdFailed(adProviderType, failedMsg)
    }

    /**
     * --------------------------- 激励广告 ---------------------------
     */
    fun callbackRewardStartRequest(@NonNull adProviderType: AdProviderType, @NonNull listener: RewardListener) {
        "${adProviderType}: 开始请求".logi()
        listener.onAdStartRequest(adProviderType)
    }

    fun callbackRewardFailed(@NonNull adProviderType: AdProviderType, @NonNull listener: RewardListener, failedMsg: String?) {
        "${adProviderType}: 请求失败了：$failedMsg".loge()
        listener.onAdFailed(adProviderType, failedMsg)
    }

    fun callbackRewardClicked(@NonNull adProviderType: AdProviderType, @NonNull listener: RewardListener) {
        "${adProviderType}: 点击了".logi()
        listener.onAdClicked(adProviderType)
    }

    fun callbackRewardShow(@NonNull adProviderType: AdProviderType, @NonNull listener: RewardListener) {
        "${adProviderType}: 展示了".logi()
        listener.onAdShow(adProviderType)
    }

    fun callbackRewardLoaded(@NonNull adProviderType: AdProviderType, @NonNull listener: RewardListener) {
        "${adProviderType}: 请求成功了".logi()
        listener.onAdLoaded(adProviderType)
    }
}