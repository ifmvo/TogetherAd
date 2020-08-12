package com.ifmvo.togetherad.gdt

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.helper.AdHelperSplash
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
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
class GdtProvider : BaseAdProvider() {

    private val TAG = "GdtProvider"

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, listener)

        val customSkipView = AdHelperSplash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        val splash = SplashAD(activity, skipView, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {

            override fun onADDismissed() {
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
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
        }, 0)
        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以调用上一个构造方法，或者给 fetchDelay 设为0。
         */
        splash.fetchAndShowIn(container)
    }

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
        //有效值就是 5-60
        mAdManager.setMaxVideoDuration(60)
        mAdManager.setMinVideoDuration(5)
        mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO)//本次拉回的视频广告，在用户看来是否为自动播放的
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