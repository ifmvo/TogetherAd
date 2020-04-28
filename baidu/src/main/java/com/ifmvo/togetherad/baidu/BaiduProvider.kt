package com.ifmvo.togetherad.baidu

import android.app.Activity
import android.view.ViewGroup
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.baidu.mobads.rewardvideo.RewardVideoAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
class BaiduProvider : BaseAdProvider() {

    private val adProviderType = AdProviderType.BAIDU

    override fun showSplashAd(activity: Activity, alias: String, container: ViewGroup, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, listener)

        SplashAd(activity, container, object : SplashAdListener {
            override fun onAdPresent() {
                callbackSplashLoaded(adProviderType, listener)
            }

            override fun onAdDismissed() {
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onAdFailed(s: String) {
                callbackSplashFailed(adProviderType, listener, "错误信息：$s")
            }

            override fun onAdClick() {
                callbackSplashClicked(adProviderType, listener)
            }

        }, TogetherAdBaidu.idMapBaidu[alias], true)
    }

    override fun getNativeAdList(activity: Activity, alias: String, maxCount: Int, listener: NativeListener) {

        callbackFlowStartRequest(adProviderType, listener)

        val baidu = BaiduNative(activity, TogetherAdBaidu.idMapBaidu[alias], object : BaiduNative.BaiduNativeNetworkListener {

            override fun onNativeLoad(list: List<NativeResponse>) {
                val subList = if (list.size > maxCount) {
                    list.subList(0, maxCount)
                } else {
                    list
                }
                callbackFlowLoaded(adProviderType, listener, subList)
            }

            override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                callbackFlowFailed(adProviderType, listener, "错误码: $nativeErrorCode")
            }
        })
        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder().build()

        baidu.makeRequest(requestParameters)
    }

    override fun isBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeResponse
    }

    override fun showNativeAd(adObject: Any, container: ViewGroup, nativeView: BaseNativeView) {

    }


    private var mRewardVideoAd : RewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        mRewardVideoAd = RewardVideoAd(activity, TogetherAdBaidu.idMapBaidu[alias], object : RewardVideoAd.RewardVideoAdListener {
            override fun onAdFailed(errorMsg: String?) {
                "onAdFailed".loge()
                callbackRewardFailed(adProviderType, listener, errorMsg)
            }

            override fun playCompletion() {
                "playCompletion".logi()
            }

            override fun onAdShow() {
                "onAdShow".logi()
                callbackRewardShow(adProviderType, listener)
            }

            override fun onAdClick() {
                "onAdClick".logi()
                callbackRewardClicked(adProviderType, listener)
            }

            override fun onAdClose(playScale: Float) {
                "onAdClose".logi()
            }

            override fun onVideoDownloadSuccess() {
                "onVideoDownloadSuccess".logi()
            }

            override fun onVideoDownloadFailed() {
                "onVideoDownloadFailed".logi()
            }
        }, false)

        mRewardVideoAd?.load()
    }

    override fun showRewardAd(activity: Activity) {
        mRewardVideoAd?.show()
    }
}