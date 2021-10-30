package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.mg.R
import com.ifmvo.togetherad.mg.TogetherAdMg
import com.mango.wakeupsdk.ManGoSDK
import com.mango.wakeupsdk.open.error.ErrorMessage
import com.mango.wakeupsdk.open.listener.OnRewardVideoListener
import com.mango.wakeupsdk.provider.SdkProviderType

abstract class MgProviderReward : MgProviderNativeExpress() {

    override fun requestAndShowRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {
        callbackRewardStartRequest(adProviderType, alias, listener)
        ManGoSDK.getInstance().rewardVideo(activity, TogetherAdMg.idMapMg[alias], object : OnRewardVideoListener {
            override fun onInstallFinished(type: SdkProviderType?, sdkId: Int) {}
            override fun onDownloadFinished(type: SdkProviderType?, sdkId: Int) {}
            override fun onClose(type: SdkProviderType?) {
                callbackRewardClosed(adProviderType, listener)
            }
            override fun onShow(type: SdkProviderType?, sdkId: Int) {
                callbackRewardShow(adProviderType, listener)
            }
            override fun onLoad(type: SdkProviderType?) {
                callbackRewardVideoCached(adProviderType, listener)
                callbackRewardLoaded(adProviderType, alias, listener)
            }
            override fun onClick(type: SdkProviderType?, sdkId: Int) {
                callbackRewardClicked(adProviderType, listener)
            }
            override fun onReward(type: SdkProviderType?) {}
            override fun onLeftApplication(type: SdkProviderType?, sdkId: Int) {}
            override fun onPlayFinished(type: SdkProviderType?, sdkId: Int) {
                callbackRewardVideoComplete(adProviderType, listener)
                callbackRewardVerify(adProviderType, listener)
            }
            override fun onError(type: SdkProviderType?, message: ErrorMessage?) {
                callbackRewardFailed(adProviderType, alias, listener, message?.code, message?.message)
            }
        })
    }

    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {
        callbackRewardStartRequest(adProviderType, alias, listener)
        callbackRewardFailed(adProviderType, alias, listener, null, "请使用requestAndShowRewardAd进行请求")
    }

    override fun showRewardAd(activity: Activity): Boolean {
        return false
    }
}