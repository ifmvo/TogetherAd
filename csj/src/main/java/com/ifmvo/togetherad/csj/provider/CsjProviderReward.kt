package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderReward : CsjProviderNativeExpress() {

    private var mttRewardVideoAd: TTRewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        val adSlotBuilder = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Reward.supportDeepLink)
                .setRewardAmount(if (CsjProvider.Reward.rewardAmount != -1) CsjProvider.Reward.rewardAmount else -1)
                .setRewardName(if (CsjProvider.Reward.rewardName?.isNotEmpty() == true) CsjProvider.Reward.rewardName else "")
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
                .setUserID(if (CsjProvider.Reward.userID?.isNotEmpty() == true) CsjProvider.Reward.userID else "")
                .setOrientation(CsjProvider.Reward.orientation)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL

        if (CsjProvider.Reward.mediaExtra?.isNotEmpty() == true) {
            adSlotBuilder.setMediaExtra(CsjProvider.Reward.mediaExtra)
        }

        TTAdSdk.getAdManager().createAdNative(activity).loadRewardVideoAd(adSlotBuilder.build(), object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String) {
                callbackRewardFailed(adProviderType, listener, code, message)
                mttRewardVideoAd = null
            }

            //视频广告加载后的视频文件资源缓存到本地的回调
            override fun onRewardVideoCached() {
                callbackRewardVideoCached(adProviderType, listener)
            }

            //视频广告素材加载到，如title,视频url等，不包括视频文件
            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd) {

                mttRewardVideoAd = ad
                mttRewardVideoAd?.setShowDownLoadBar(CsjProvider.Reward.showDownLoadBar)
                mttRewardVideoAd?.setRewardAdInteractionListener(object : TTRewardVideoAd.RewardAdInteractionListener {
                    override fun onSkippedVideo() {
                    }

                    override fun onVideoError() {
                    }

                    override fun onAdShow() {
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onAdVideoBarClick() {
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onAdClose() {
                        callbackRewardClosed(adProviderType, listener)
                        mttRewardVideoAd = null
                    }

                    override fun onVideoComplete() {
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String?, errorCode: Int, errorMsg: String?) {
                        CsjProvider.Reward.rewardVerify = rewardVerify
                        CsjProvider.Reward.rewardAmount = rewardAmount
                        CsjProvider.Reward.rewardName = rewardName
                        CsjProvider.Reward.errorCode = errorCode
                        CsjProvider.Reward.errorMsg = errorMsg
                        callbackRewardVerify(adProviderType, listener)
                    }
                })
                mttRewardVideoAd?.setDownloadListener(object : TTAppDownloadListener {
                    override fun onIdle() {
                    }

                    override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onInstalled(fileName: String?, appName: String?) {
                    }
                })

                callbackRewardLoaded(adProviderType, listener)
            }
        })
    }

    override fun showRewardAd(activity: Activity) {
        mttRewardVideoAd?.showRewardVideoAd(activity)
    }

}