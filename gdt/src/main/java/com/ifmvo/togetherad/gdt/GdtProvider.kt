package com.ifmvo.togetherad.gdt

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
import com.ifmvo.togetherad.core.helper.AdHelperSplash
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.ads.cfg.BrowserType
import com.qq.e.ads.cfg.DownAPPConfirmPolicy
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
import com.qq.e.ads.nativ.NativeADUnifiedListener
import com.qq.e.ads.nativ.NativeUnifiedAD
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.ads.rewardvideo.RewardVideoAD
import com.qq.e.ads.rewardvideo.RewardVideoADListener
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import kotlin.math.roundToInt


/**
 * 广告提供商：优量汇（广点通）
 *
 * Created by Matthew Chen on 2020-04-03.
 */
open class GdtProvider : BaseAdProvider() {

    private val TAG = "GdtProvider"

    /**
     * --------------------------- 开屏 ---------------------------
     */
    object Splash {

        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以给 fetchDelay 设为0。
         */
        var maxFetchDelay = 0

        //自定义按钮
        var customSkipView: BaseSplashSkipView? = null

    }

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, listener)

        val customSkipView = Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        val splash = SplashAD(activity, skipView, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {

            override fun onADDismissed() {
                Splash.customSkipView = null
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                Splash.customSkipView = null
                callbackSplashFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            /**
             * 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
             */
            override fun onADPresent() {
                activity.runOnUiThread {
                    skipView?.run {
                        container.addView(this, customSkipView.getLayoutParams())
                    }
                }
                "${adProviderType}: 广告成功展示".logi(TAG)
            }

            override fun onADClicked() {
                callbackSplashClicked(adProviderType, listener)
            }

            override fun onADTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000f).roundToInt()
                customSkipView?.handleTime(second)
                "${adProviderType}: 倒计时: $second".logv(TAG)
            }

            override fun onADExposure() {
                callbackSplashExposure(adProviderType, listener)
            }

            /**
             * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，否则在showAd时会返回广告超时错误。
             */
            override fun onADLoaded(expireTimestamp: Long) {
                callbackSplashLoaded(adProviderType, listener)
            }
        }, Splash.maxFetchDelay)
        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以调用上一个构造方法，或者给 fetchDelay 设为0。
         */
        splash.fetchAndShowIn(container)
    }

    /**
     * --------------------------- Banner横幅 ---------------------------
     */
    private var banner: UnifiedBannerView? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, listener)
        destroyBannerAd()
        banner = UnifiedBannerView(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedBannerADListener {
            override fun onADCloseOverlay() {
                "onADCloseOverlay".logi(TAG)
            }

            override fun onADExposure() {
                callbackBannerExpose(adProviderType, listener)
            }

            override fun onADClosed() {
                callbackBannerClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
                "onADLeftApplication".logi(TAG)
            }

            override fun onADOpenOverlay() {
                "onADOpenOverlay".logi(TAG)
            }

            override fun onNoAD(adError: AdError?) {
                banner?.destroy()
                callbackBannerFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            override fun onADReceive() {
                callbackBannerLoaded(adProviderType, listener)
            }

            override fun onADClicked() {
                callbackBannerClicked(adProviderType, listener)
            }
        })
        container.addView(banner)
        banner?.loadAD()
    }

    override fun destroyBannerAd() {
        banner?.destroy()
        banner = null
    }

    /**
     * --------------------------- Inter插屏 ---------------------------
     */
    private var interAd: UnifiedInterstitialAD? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, listener)

        destroyInterAd()

        interAd = UnifiedInterstitialAD(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedInterstitialADListener {
            override fun onADExposure() {
                callbackInterExpose(adProviderType, listener)
            }

            override fun onVideoCached() {
                "onVideoCached".logi(TAG)
            }

            override fun onADOpened() {
                "onADOpened".logi(TAG)
            }

            override fun onADClosed() {
                callbackInterClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
                "onADLeftApplication".logi(TAG)
            }

            override fun onADReceive() {
                callbackInterLoaded(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                callbackInterFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            override fun onADClicked() {
                callbackInterClicked(adProviderType, listener)
            }
        })
        interAd?.loadAD()
    }

    override fun showInterAd(activity: Activity) {
        interAd?.show()
    }

    override fun destroyInterAd() {
        interAd?.close()
        interAd?.destroy()
        interAd = null
    }

    /**
     * --------------------------- 原生自渲染 ---------------------------
     */
    object Native {
        //设置返回视频广告的最大视频时长（闭区间，可单独设置），单位:秒，合法输入为：5<=maxVideoDuration<=60. 此设置会影响广告填充，请谨慎设置
        var maxVideoDuration = 60

        //设置返回视频广告的最小视频时长（闭区间，可单独设置），单位:秒 此设置会影响广告填充，请谨慎设置
        var minVideoDuration = 5

        //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认，SDK 按照默认逻辑选择)
        var browserType: BrowserType = BrowserType.Default

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非 wifi 展示），NoConfirm（所有情况不展示）
        var downAPPConfirmPolicy: DownAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //测试性接口，不保证有效。传入 app 内类目信息
        var categories: List<String>? = null

        //设置本次拉取的视频广告，从用户角度看到的视频播放策略；
        // 可选项包括自VideoOption.VideoPlayPolicy.AUTO(在用户看来，视频广告是自动播放的)和VideoOption.VideoPlayPolicy.MANUAL(在用户看来，视频广告是手动播放的)；
        // 如果广告位支持视频，强烈建议调用此接口设置视频广告的播放策略，有助于提高eCPM值；如果广告位不支持视频，忽略本接口
        var videoPlayPolicy: Int = VideoOption.VideoPlayPolicy.AUTO
    }

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

        callbackFlowStartRequest(adProviderType, listener)

        val nativeADUnifiedListener = object : NativeADUnifiedListener {
            override fun onADLoaded(adList: List<NativeUnifiedADData>?) {
                //list是空的，按照错误来处理
                if (adList?.isEmpty() != false) {
                    callbackFlowFailed(adProviderType, listener, "请求成功，但是返回的list为空")
                    return
                }

                callbackFlowLoaded(adProviderType, listener, adList)
            }

            override fun onNoAD(adError: AdError?) {
                callbackFlowFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }
        }

        val mAdManager = NativeUnifiedAD(activity, TogetherAdGdt.idMapGDT[alias], nativeADUnifiedListener)
        mAdManager.setBrowserType(Native.browserType)
        mAdManager.setDownAPPConfirmPolicy(Native.downAPPConfirmPolicy)
        Native.categories?.let { mAdManager.setCategories(it) }
        mAdManager.setMaxVideoDuration(Native.maxVideoDuration)//有效值就是 5-60
        mAdManager.setMinVideoDuration(Native.minVideoDuration)
        mAdManager.setVideoPlayPolicy(Native.videoPlayPolicy)//本次拉回的视频广告，在用户看来是否为自动播放的
        mAdManager.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK)//视频播放前，用户看到的广告容器是由SDK渲染的
        mAdManager.loadData(maxCount)
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeUnifiedADData
    }

    override fun resumeNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        adObject.resume()
        if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {
            adObject.resumeVideo()
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {
            adObject.pauseVideo()
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        adObject.destroy()
    }

    /**
     * --------------------------- 激励 ---------------------------
     */
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
                callbackRewardFailed(adProviderType, listener, "错误码: ${adError?.errorCode}}, 错误信息：${adError?.errorMsg}")
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