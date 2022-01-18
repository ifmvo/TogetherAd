package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.ks.R
import com.ifmvo.togetherad.ks.TogetherAdKs
import com.kwad.sdk.api.KsLoadManager
import com.kwad.sdk.api.KsRewardVideoAd
import com.kwad.sdk.api.KsScene
import com.kwad.sdk.api.KsVideoPlayConfig

abstract class KsProviderReward : KsProviderNativeExpress() {

    private var rewardVideoAd: KsRewardVideoAd? = null

    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, alias, listener)

        if (TogetherAdKs.adRequestManager == null) {
            callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val scene = KsScene.Builder(TogetherAdKs.idMapKs[alias] ?: 0).build()
        // 请求的期望屏幕方向传递为1，表示期望为竖屏
        TogetherAdKs.adRequestManager!!.loadRewardVideoAd(scene, object : KsLoadManager.RewardVideoAdListener {
            override fun onError(code: Int, msg: String) {
                rewardVideoAd = null
                callbackRewardFailed(adProviderType, alias, listener, code, msg)
            }

            override fun onRequestResult(adNumber: Int) {}

            override fun onRewardVideoAdLoad(adList: List<KsRewardVideoAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                rewardVideoAd = adList[0]

                if (rewardVideoAd == null) {
                    callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                callbackRewardLoaded(adProviderType, alias, listener)

                rewardVideoAd?.setRewardAdInteractionListener(object : KsRewardVideoAd.RewardAdInteractionListener {
                    override fun onAdClicked() {
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onPageDismiss() {
                        rewardVideoAd = null
                        callbackRewardClosed(adProviderType, listener)
                    }

                    override fun onVideoPlayError(errorCode: Int, extra: Int) {
                        callbackRewardFailed(adProviderType, alias, listener, errorCode, extra.toString())
                    }

                    override fun onVideoSkipToEnd(p0: Long) {}
                    override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {}

                    override fun onVideoPlayEnd() {
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onVideoPlayStart() {
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onRewardVerify() {
                        callbackRewardVerify(adProviderType, listener)
                    }
                })
            }
        })
    }

    override fun showRewardAd(activity: Activity): Boolean {
        if (rewardVideoAd?.isAdEnable == true) {
            val config = KsVideoPlayConfig.Builder()
                    .showLandscape(KsProvider.Reward.isShowLandscape)
                    .build()
            rewardVideoAd!!.showRewardVideoAd(activity, config)
            return true
        }
        return false
    }

    override fun requestAndShowRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {
        callbackRewardStartRequest(adProviderType, alias, listener)

        if (TogetherAdKs.adRequestManager == null) {
            callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val scene = KsScene.Builder(TogetherAdKs.idMapKs[alias] ?: 0).build()
        // 请求的期望屏幕方向传递为1，表示期望为竖屏
        TogetherAdKs.adRequestManager!!.loadRewardVideoAd(scene, object : KsLoadManager.RewardVideoAdListener {
            override fun onError(code: Int, msg: String) {
                rewardVideoAd = null
                callbackRewardFailed(adProviderType, alias, listener, code, msg)
            }

            override fun onRequestResult(adNumber: Int) {}

            override fun onRewardVideoAdLoad(adList: List<KsRewardVideoAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                rewardVideoAd = adList[0]

                if (rewardVideoAd == null) {
                    callbackRewardFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                callbackRewardLoaded(adProviderType, alias, listener)

                rewardVideoAd?.setRewardAdInteractionListener(object : KsRewardVideoAd.RewardAdInteractionListener {
                    override fun onAdClicked() {
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onPageDismiss() {
                        rewardVideoAd = null
                        callbackRewardClosed(adProviderType, listener)
                    }

                    override fun onVideoPlayError(errorCode: Int, extra: Int) {
                        callbackRewardFailed(adProviderType, alias, listener, errorCode, extra.toString())
                    }

                    override fun onVideoSkipToEnd(p0: Long) {}
                    override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {}

                    override fun onVideoPlayEnd() {
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onVideoPlayStart() {
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onRewardVerify() {
                        callbackRewardVerify(adProviderType, listener)
                    }
                })

                showRewardAd(activity)
            }
        })
    }
}