package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.ads.rewardvideo.ServerSideVerificationOptions
import com.qq.e.comm.util.AdError
import com.qq.e.comm.util.VideoAdValidity


/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderReward : GdtProviderNativeExpress2() {

    private var rewardVideoAD: RewardVideoAD? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, alias, listener)

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

            override fun onReward(map: MutableMap<String, Any>?) {
                map?.let { GdtProvider.Reward.verificationOption = it[ServerSideVerificationOptions.TRANS_ID] }
                callbackRewardVerify(adProviderType, listener)
            }

            override fun onADClose() {
                callbackRewardClosed(adProviderType, listener)
                rewardVideoAD = null
            }

            override fun onADLoad() {
                TogetherAdGdt.downloadConfirmListener?.let {
                    rewardVideoAD?.setDownloadConfirmListener(it)
                }
                callbackRewardLoaded(adProviderType, alias, listener)
            }

            override fun onVideoComplete() {
                callbackRewardVideoComplete(adProviderType, listener)
            }

            override fun onError(adError: AdError?) {
                callbackRewardFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
                rewardVideoAD = null
            }

            override fun onADShow() {
                callbackRewardShow(adProviderType, listener)
            }

        }, GdtProvider.Reward.volumeOn)

        rewardVideoAD?.loadAD()
    }

    override fun showRewardAd(activity: Activity): Boolean {
        //空的就展示失败
        if (rewardVideoAD == null) {
            return false
        }

        return when (rewardVideoAD!!.checkValidity()!!) {
            //已经展示或过期
            VideoAdValidity.SHOWED, VideoAdValidity.OVERDUE -> {
                false
            }
            //有效或未缓存完成，直接展示
            VideoAdValidity.VALID, VideoAdValidity.NONE_CACHE -> {
                rewardVideoAD?.showAD()
                true
            }
        }
    }

}