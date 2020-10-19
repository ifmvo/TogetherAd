package com.ifmvo.togetherad.csj

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi


/**
 * 广告提供商：穿山甲
 *
 * Created by Matthew Chen on 2020-04-03.
 */
class CsjProvider : BaseAdProvider() {

    private val TAG = "CsjProvider"

    /**
     * --------------------------- 开屏 ---------------------------
     */
    object Splash {

        //超时时间
        var maxFetchDelay = 3000

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 1080

        internal var imageAcceptedSizeHeight = 1920

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }
    }

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, listener)
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(Splash.supportDeepLink)
                .setImageAcceptedSize(Splash.imageAcceptedSizeWidth, Splash.imageAcceptedSizeHeight)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, listener, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, listener)

                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, listener, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, listener, "错误码：$errorCode, 错误信息：$errorMsg")
            }
        }, Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    /**
     * --------------------------- 横幅Banner ---------------------------
     */
    object Banner {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 257

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {

        callbackBannerStartRequest(adProviderType, listener)

        destroyBannerAd()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(Banner.supportDeepLink)
                .setImageAcceptedSize(Banner.imageAcceptedSizeWidth, Banner.imageAcceptedSizeHeight)
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadBannerAd(adSlot, object : TTAdNative.BannerAdListener {
            override fun onBannerAdLoad(bannerAd: TTBannerAd?) {
                if (bannerAd == null) {
                    callbackBannerFailed(adProviderType, listener, "请求成功，但是返回的 bannerAd 为空")
                    return
                }

                val bannerView = bannerAd.bannerView
                if (bannerView == null) {
                    callbackBannerFailed(adProviderType, listener, "请求成功，但是返回的 bannerView 为空")
                    return
                }

                callbackBannerLoaded(adProviderType, listener)

                bannerAd.setSlideIntervalTime(Banner.slideIntervalTime)
                container.removeAllViews()
                container.addView(bannerView)

                bannerAd.setBannerInteractionListener(object : TTBannerAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, type: Int) {
                        callbackBannerClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, type: Int) {
                        callbackBannerExpose(adProviderType, listener)
                    }
                })

                bannerAd.setShowDislikeIcon(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String?) {
                        container.removeAllViews()
                        callbackBannerClosed(adProviderType, listener)
                    }

                    override fun onCancel() {}
                    override fun onRefuse() {}
                })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                "onError".loge(TAG)
                callbackBannerFailed(adProviderType, listener, "错误码：$errorCode, 错误信息：$errorMsg")
            }
        })
    }

    override fun destroyBannerAd() {

    }

    /**
     * --------------------------- 插屏 ---------------------------
     */
    object Inter {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 600

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }
    }

    private var mTtInteractionAd: TTInteractionAd? = null
    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {

        callbackInterStartRequest(adProviderType, listener)

        destroyInterAd()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(Inter.supportDeepLink)
                .setImageAcceptedSize(Inter.imageAcceptedSizeWidth, Inter.imageAcceptedSizeHeight) //根据广告平台选择的尺寸，传入同比例尺寸
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadInteractionAd(adSlot, object : TTAdNative.InteractionAdListener {
            override fun onError(errorCode: Int, errorMsg: String?) {
                //出错
                callbackInterFailed(adProviderType, listener, "错误码: $errorCode}, 错误信息：$errorMsg")
            }

            override fun onInteractionAdLoad(ttInteractionAd: TTInteractionAd?) {
                //填充
                callbackInterLoaded(adProviderType, listener)

                mTtInteractionAd = ttInteractionAd
                mTtInteractionAd?.setAdInteractionListener(object : TTInteractionAd.AdInteractionListener {
                    override fun onAdDismiss() {
                        //消失
                        callbackInterClosed(adProviderType, listener)
                    }

                    override fun onAdClicked() {
                        //点击
                        callbackInterClicked(adProviderType, listener)
                    }

                    override fun onAdShow() {
                        //曝光
                        callbackInterExpose(adProviderType, listener)
                    }
                })

                if (mTtInteractionAd?.interactionType == TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
                    mTtInteractionAd?.setDownloadListener(object : TTAppDownloadListener {
                        override fun onIdle() {
                            "onIdle".logi(TAG)
                        }

                        override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                            "onDownloadPaused".logi(TAG)
                        }

                        override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                            "onDownloadFailed".loge(TAG)
                        }

                        override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                            "onDownloadActive".logi(TAG)
                        }

                        override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                            "onDownloadFinished".logi(TAG)
                        }

                        override fun onInstalled(fileName: String?, appName: String?) {
                            "onInstalled".logi(TAG)
                        }
                    })
                }
            }
        })
    }

    override fun showInterAd(activity: Activity) {
        mTtInteractionAd?.showInteractionAd(activity)
    }

    override fun destroyInterAd() {
        mTtInteractionAd = null
    }

    /**
     * --------------------------- 原生自渲染 ---------------------------
     */
    object Native {
        //如果需要使用穿山甲的原生广告，必须在请求之前设置类型。
        var nativeAdType = -1

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 1080

        internal var imageAcceptedSizeHeight = 1080 * 9 / 16

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }
    }

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        if (Native.nativeAdType == -1) {
            throw IllegalArgumentException(
                    """
    |-------------------------------------------------------------------------------------- 
    |  必须在每次请求穿山甲的原生广告之前设置类型。
    |  设置方式：CsjProvider.Native.nativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_INTERACTION_AD
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_BANNER
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_CACHED_SPLASH
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_DRAW_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_REWARD_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_SPLASH
    |--------------------------------------------------------------------------------------

"""
            )
        }

        callbackFlowStartRequest(adProviderType, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(Native.supportDeepLink)
                .setImageAcceptedSize(Native.imageAcceptedSizeWidth, Native.imageAcceptedSizeHeight)
                .setNativeAdType(Native.nativeAdType)
                .setAdCount(maxCount)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onNativeAdLoad(adList: MutableList<TTNativeAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackFlowFailed(adProviderType, listener, "请求成功，但是返回的list为空")
                    return
                }

                callbackFlowLoaded(adProviderType, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackFlowFailed(adProviderType, listener, "错误码: $errorCode}, 错误信息：$errorMsg")
            }
        })
    }

    override fun resumeNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTNativeAd
    }

    /**
     * --------------------------- 激励 ---------------------------
     */
    object Reward {
        //表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
        var userID: String? = null

        var supportDeepLink: Boolean = true

        //奖励的名称
        var rewardName: String? = null

        //奖励的数量
        var rewardAmount: Int = -1

        //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
        var orientation: Int = TTAdConstant.VERTICAL

        //用户透传的信息，可不传
        var mediaExtra: String? = null

        //设置是否在视频播放页面展示下载bar
        var showDownLoadBar: Boolean = true

        //onAdRewardVerify 回调中用于判断校验结果（ 后台校验中用到 ）
        var rewardVerify: Boolean = false
            internal set
    }

    private var mttRewardVideoAd: TTRewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        val adSlotBuilder = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(Reward.supportDeepLink)
                .setRewardAmount(if (Reward.rewardAmount != -1) Reward.rewardAmount else -1)
                .setRewardName(if (Reward.rewardName?.isNotEmpty() == true) Reward.rewardName else "")
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
                .setUserID(if (Reward.userID?.isNotEmpty() == true) Reward.userID else "")
                .setOrientation(Reward.orientation)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL

        if (Reward.mediaExtra?.isNotEmpty() == true) {
            adSlotBuilder.setMediaExtra(Reward.mediaExtra)
        }

        TTAdSdk.getAdManager().createAdNative(activity).loadRewardVideoAd(adSlotBuilder.build(), object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String) {
                "onError".loge(TAG)
                callbackRewardFailed(adProviderType, listener, "错误码: $code, 错误信息：$message")
                mttRewardVideoAd = null
            }

            //视频广告加载后的视频文件资源缓存到本地的回调
            override fun onRewardVideoCached() {
                "onRewardVideoCached".logi(TAG)
                callbackRewardVideoCached(adProviderType, listener)
            }

            //视频广告素材加载到，如title,视频url等，不包括视频文件
            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd) {
                "onRewardVideoAdLoad".logi(TAG)

                mttRewardVideoAd = ad
                mttRewardVideoAd?.setShowDownLoadBar(Reward.showDownLoadBar)
                mttRewardVideoAd?.setRewardAdInteractionListener(object : TTRewardVideoAd.RewardAdInteractionListener {
                    override fun onSkippedVideo() {
                        "onSkippedVideo".logi(TAG)
                    }

                    override fun onVideoError() {
                        "onVideoError".loge(TAG)
                    }

                    override fun onAdShow() {
                        "onAdShow".logi(TAG)
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onAdVideoBarClick() {
                        "onAdVideoBarClick".logi(TAG)
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onAdClose() {
                        "onAdClose".logi(TAG)
                        callbackRewardClosed(adProviderType, listener)
                        mttRewardVideoAd = null
                    }

                    override fun onVideoComplete() {
                        "onVideoComplete".logi(TAG)
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String) {
                        "verify:$rewardVerify amount:$rewardAmount name:$rewardName".logi(TAG)
                        Reward.rewardVerify = rewardVerify
                        callbackRewardVerify(adProviderType, listener)
                    }
                })
                mttRewardVideoAd?.setDownloadListener(object : TTAppDownloadListener {
                    override fun onIdle() {
                        "onIdle".logi(TAG)
                    }

                    override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        "onDownloadActive".logi(TAG)
                    }

                    override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        "onDownloadPaused".logi(TAG)
                    }

                    override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                        "onDownloadFailed".loge(TAG)
                    }

                    override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                        "onDownloadFinished".logi(TAG)
                    }

                    override fun onInstalled(fileName: String?, appName: String?) {
                        "onInstalled".logi(TAG)
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
