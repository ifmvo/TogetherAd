package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.comm.util.AdError

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderReward : GdtProviderNativeExpress() {

    private var rewardVideoAD: RewardVideoAD? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        rewardVideoAD = RewardVideoAD(activity, TogetherAdGdt.idMapGDT[alias], object : RewardVideoADListener {

            override fun onADExpose() {
                callbackRewardExpose(adProviderType, listener)
            }

            override fun onADClick() {
                callbackRewardClicked(adProviderType, listener)
            }

            override fun onVideoCached() {
                callbackRewardVideoCached(adProviderType, listener)
            }

            override fun onReward() {
                callbackRewardVerify(adProviderType, listener)
            }

            override fun onADClose() {
                callbackRewardClosed(adProviderType, listener)
                rewardVideoAD = null
            }

            override fun onADLoad() {
                callbackRewardLoaded(adProviderType, listener)
            }

            override fun onVideoComplete() {
                callbackRewardVideoComplete(adProviderType, listener)
            }

            override fun onError(adError: AdError?) {
                callbackRewardFailed(adProviderType, listener, adError?.errorCode, adError?.errorMsg)
                rewardVideoAD = null
            }

            override fun onADShow() {
                callbackRewardShow(adProviderType, listener)
            }

        }, false)
        rewardVideoAD?.loadAD()
    }

    override fun showRewardAd(activity: Activity) {
        rewardVideoAD?.showAD()
    }

}