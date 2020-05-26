package com.ifmvo.togetherad.baidu

import android.app.Activity
import android.view.ViewGroup
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.baidu.mobads.AdView
import com.baidu.mobads.AdViewListener
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.baidu.mobads.rewardvideo.RewardVideoAd
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import org.json.JSONObject


/**
 * 广告提供商：百青藤
 *
 * Created by Matthew Chen on 2020-04-03.
 */
class BaiduProvider : BaseAdProvider() {

    private val TAG = "BaiduProvider"

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {
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

    private var adView: AdView? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, listener)

        adView = AdView(activity, TogetherAdBaidu.idMapBaidu[alias])
        adView?.setListener(object : AdViewListener {
            override fun onAdFailed(errorMsg: String?) {
                callbackBannerFailed(adProviderType, listener, errorMsg)
            }

            override fun onAdShow(p0: JSONObject?) {
                callbackBannerExpose(adProviderType, listener)
            }

            override fun onAdClick(p0: JSONObject?) {
                callbackBannerClicked(adProviderType, listener)
            }

            override fun onAdReady(p0: AdView?) {
                callbackBannerLoaded(adProviderType, listener)
            }

            override fun onAdSwitch() {
                "onAdSwitch".logi(TAG)
            }

            override fun onAdClose(p0: JSONObject?) {
                callbackBannerClose(adProviderType, listener)
            }
        })
        container.addView(adView)
    }

    override fun destroyBannerAd() {
        adView?.destroy()
    }

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

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

    private var mRewardVideoAd: RewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        mRewardVideoAd = RewardVideoAd(activity, TogetherAdBaidu.idMapBaidu[alias], object : RewardVideoAd.RewardVideoAdListener {
            override fun onAdFailed(errorMsg: String?) {
                "onAdFailed".loge(TAG)
                callbackRewardFailed(adProviderType, listener, errorMsg)
                mRewardVideoAd = null
            }

            override fun playCompletion() {
                "playCompletion".logi(TAG)
                callbackRewardVideoComplete(adProviderType, listener)
                callbackRewardVerify(adProviderType, listener)
            }

            override fun onAdShow() {
                "onAdShow".logi(TAG)
                callbackRewardShow(adProviderType, listener)
                callbackRewardExpose(adProviderType, listener)
            }

            override fun onAdClick() {
                "onAdClick".logi(TAG)
                callbackRewardClicked(adProviderType, listener)
            }

            override fun onAdClose(playScale: Float) {
                "onAdClose".logi(TAG)
                callbackRewardClose(adProviderType, listener)
                mRewardVideoAd = null
            }

            override fun onVideoDownloadSuccess() {
                "onVideoDownloadSuccess".logi(TAG)
                callbackRewardLoaded(adProviderType, listener)
                callbackRewardVideoCached(adProviderType, listener)
            }

            override fun onVideoDownloadFailed() {
                "onVideoDownloadFailed".loge(TAG)
                callbackRewardFailed(adProviderType, listener, "视频缓存失败")
            }
        }, false)

        mRewardVideoAd?.load()
    }

    override fun showRewardAd(activity: Activity) {
        mRewardVideoAd?.show()
    }
}