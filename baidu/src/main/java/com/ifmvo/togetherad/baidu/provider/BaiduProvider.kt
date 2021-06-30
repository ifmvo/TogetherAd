package com.ifmvo.togetherad.baidu.provider

import android.app.Activity
import android.view.ViewGroup
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.baidu.mobads.*
import com.baidu.mobads.rewardvideo.RewardVideoAd
import com.ifmvo.togetherad.baidu.TogetherAdBaidu
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import org.json.JSONObject


/**
 * 广告提供商：百青藤
 *
 * Created by Matthew Chen on 2020-04-03.
 */
open class BaiduProvider : BaseAdProvider() {

    object Splash {

        //超时时间
        var maxFetchDelay = 4000

    }

    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(adProviderType, alias, listener, null, "百度开屏不支持加载和展示分开")
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        return false
    }

    /**
     * --------------------------- 开屏 ---------------------------
     */
    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)

        SplashAd(activity, container, object : SplashAdListener {
            override fun onAdPresent() {
                "onADLoaded".logd(tag)
                callbackSplashLoaded(adProviderType, alias, listener)
            }

            override fun onAdDismissed() {
                "onADLoaded".logd(tag)

                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onADLoaded() {
                "onADLoaded".logd(tag)
            }

            override fun onAdFailed(errorMsg: String) {
                "onADLoaded".logd(tag)
                callbackSplashFailed(adProviderType, alias, listener, null, errorMsg)
            }

            override fun onAdClick() {
                "onADLoaded".logd(tag)
                callbackSplashClicked(adProviderType, listener)
            }

        }, TogetherAdBaidu.idMapBaidu[alias], true, Splash.maxFetchDelay)
    }

    /**
     * --------------------------- Banner横幅 ---------------------------
     */
    private var adView: AdView? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, alias, listener)

        adView = AdView(activity, TogetherAdBaidu.idMapBaidu[alias])
        adView?.setListener(object : AdViewListener {
            override fun onAdFailed(errorMsg: String?) {
                callbackBannerFailed(adProviderType, alias, listener, null, errorMsg)
            }

            override fun onAdShow(p0: JSONObject?) {
                callbackBannerExpose(adProviderType, listener)
            }

            override fun onAdClick(p0: JSONObject?) {
                callbackBannerClicked(adProviderType, listener)
            }

            override fun onAdReady(p0: AdView?) {
                callbackBannerLoaded(adProviderType, alias, listener)
            }

            override fun onAdSwitch() {
                "onAdSwitch".logi(tag)
            }

            override fun onAdClose(p0: JSONObject?) {
                callbackBannerClosed(adProviderType, listener)
            }
        })
        container.addView(adView)
    }

    override fun destroyBannerAd() {
        adView?.destroy()
    }

    /**
     * --------------------------- Inter插屏 ---------------------------
     */
    private var mInterAd: InterstitialAd? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, alias, listener)
        "onStartRequest".logd(tag)
        destroyInterAd()

        mInterAd = InterstitialAd(activity, TogetherAdBaidu.idMapBaidu[alias])
        mInterAd?.setListener(object : InterstitialAdListener {
            override fun onAdFailed(errorMsg: String?) {
                "onAdFailed".logd(tag)
                callbackInterFailed(adProviderType, alias, listener, null, errorMsg)
            }

            override fun onAdDismissed() {
                "onAdDismissed".logd(tag)
                callbackInterClosed(adProviderType, listener)
            }

            override fun onAdPresent() {
                "onAdPresent".logd(tag)
                callbackInterExpose(adProviderType, listener)
            }

            override fun onAdClick(inter: InterstitialAd?) {
                "onAdClick".logd(tag)
                callbackInterClicked(adProviderType, listener)
            }

            override fun onAdReady() {
                "onAdReady".logd(tag)
                callbackInterLoaded(adProviderType, alias, listener)
            }
        })
        mInterAd?.loadAd()
    }

    override fun showInterAd(activity: Activity) {
        mInterAd?.showAd(activity)
    }

    override fun destroyInterAd() {
        mInterAd?.destroy()
        mInterAd = null
    }

    /**
     * --------------------------- 原生自渲染 ---------------------------
     */
    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

        callbackNativeStartRequest(adProviderType, alias, listener)

        val baidu = BaiduNative(activity, TogetherAdBaidu.idMapBaidu[alias], object : BaiduNative.BaiduNativeNetworkListener {

            override fun onNativeLoad(list: List<NativeResponse>) {
                val subList = if (list.size > maxCount) {
                    list.subList(0, maxCount)
                } else {
                    list
                }
                callbackNativeLoaded(adProviderType, alias, listener, subList)
            }

            override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                val errorMsg = when (nativeErrorCode) {
                    NativeErrorCode.LOAD_AD_FAILED -> {
                        "请求失败"
                    }
                    NativeErrorCode.CONFIG_ERROR -> {
                        "配置错误"
                    }
                    NativeErrorCode.INTERNAL_ERROR -> {
                        "内部错误"
                    }
                    NativeErrorCode.UNKNOWN -> {
                        "未知错误"
                    }
                }
                callbackNativeFailed(adProviderType, alias, listener, null, errorMsg)
            }
        })
        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder().build()

        baidu.makeRequest(requestParameters)
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeResponse
    }

    override fun resumeNativeAd(adObject: Any) {
        when (adObject) {
            is NativeResponse -> {

            }
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        when (adObject) {
            is NativeResponse -> {

            }
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        when (adObject) {
            is NativeResponse -> {

            }
        }
    }

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {
        callbackNativeExpressStartRequest(adProviderType, alias, listener)
        callbackNativeExpressFailed(adProviderType, alias, listener, null, "百度不支持原生模板类型广告")
    }

    override fun destroyNativeExpressAd(adObject: Any) {

    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }

    /**
     * --------------------------- 激励 ---------------------------
     */
    private var mRewardVideoAd: RewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, alias, listener)

        mRewardVideoAd = RewardVideoAd(activity, TogetherAdBaidu.idMapBaidu[alias], object : RewardVideoAd.RewardVideoAdListener {
            override fun onAdFailed(errorMsg: String?) {
                "onAdFailed".loge(tag)
                callbackRewardFailed(adProviderType, alias, listener, null, errorMsg)
                mRewardVideoAd = null
            }

            override fun playCompletion() {
                "playCompletion".logi(tag)
                callbackRewardVideoComplete(adProviderType, listener)
                callbackRewardVerify(adProviderType, listener)
            }

            override fun onAdShow() {
                "onAdShow".logi(tag)
                callbackRewardShow(adProviderType, listener)
                callbackRewardExpose(adProviderType, listener)
            }

            override fun onAdClick() {
                "onAdClick".logi(tag)
                callbackRewardClicked(adProviderType, listener)
            }

            override fun onAdClose(playScale: Float) {
                "onAdClose".logi(tag)
                callbackRewardClosed(adProviderType, listener)
                mRewardVideoAd = null
            }

            override fun onVideoDownloadSuccess() {
                "onVideoDownloadSuccess".logi(tag)
                callbackRewardLoaded(adProviderType, alias, listener)
                callbackRewardVideoCached(adProviderType, listener)
            }

            override fun onVideoDownloadFailed() {
                "onVideoDownloadFailed".loge(tag)
                callbackRewardFailed(adProviderType, alias, listener, null, "视频缓存失败")
            }
        }, false)

        mRewardVideoAd?.load()
    }

    override fun showRewardAd(activity: Activity): Boolean {
        if (mRewardVideoAd?.isReady == true) {
            mRewardVideoAd?.show()
            return true
        }
        return false
    }

    override fun requestFullVideoAd(activity: Activity, adProviderType: String, alias: String, listener: FullVideoListener) {
        callbackFullVideoStartRequest(adProviderType, alias, listener)
        callbackFullVideoFailed(adProviderType, alias, listener, null, "百度不支持全屏视频广告")
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        return false
    }
}