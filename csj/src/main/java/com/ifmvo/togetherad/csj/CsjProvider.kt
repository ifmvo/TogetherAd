package com.ifmvo.togetherad.csj

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.TTAdDislike.DislikeInteractionCallback
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
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

    override fun showSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, listener)

        val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(point.x, point.y)
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
        }, 2500)//超时时间，demo 为 2000
    }

    private var mTTAd: TTNativeExpressAd? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, listener)

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dip(pxValue: Int): Float {
            val scale = activity.resources.displayMetrics.density
            return pxValue / scale + 0.5f
        }

        val wDp = px2dip(dm.widthPixels)
        val hDp = wDp / 4 * 9 / 16

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(wDp, hDp) //期望模板广告view的size,单位dp
                .setImageAcceptedSize(350, 350)//这个参数设置即可，不影响模板广告的size
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadBannerExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
                "onNativeExpressAdLoad".logi(TAG)
                if (adList.isNullOrEmpty()) {
                    callbackBannerFailed(adProviderType, listener, "请求成功，但是返回的list为空")
                    return
                }
                callbackBannerLoaded(adProviderType, listener)

                mTTAd = adList[0]
                mTTAd?.setSlideIntervalTime(30 * 1000)
                mTTAd?.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
                    override fun onAdClicked(p0: View?, p1: Int) {
                        "onAdClicked".logi(TAG)
                        callbackBannerClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, p1: Int) {
                        "onAdShow".logi(TAG)
                        callbackBannerExpose(adProviderType, listener)
                    }

                    override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                        "onRenderSuccess".logi(TAG)
                        container.addView(view)
                    }

                    override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
                        "onRenderFail".logi(TAG)
                        callbackBannerFailed(adProviderType, listener, "错误码：$errorCode, 错误信息：$errorMsg")
                    }
                })
                mTTAd?.setDislikeCallback(activity, object : DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String) {
                        //用户选择不喜欢原因后，移除广告展示
                        container.removeAllViews()
                        callbackBannerClose(adProviderType, listener)
                    }

                    override fun onCancel() {}
                })
                mTTAd?.render()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                "onError".logi(TAG)
                callbackBannerFailed(adProviderType, listener, "错误码：$errorCode, 错误信息：$errorMsg")
            }
        })
    }

    override fun destroyBannerAd() {
        mTTAd?.destroy()
    }


    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        callbackFlowStartRequest(adProviderType, listener)

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(dm.widthPixels, (dm.widthPixels * 9 / 16))
                .setAdCount(maxCount)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadFeedAd(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(adList: MutableList<TTFeedAd>?) {

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

    override fun isBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTFeedAd
    }

    private var mttRewardVideoAd: TTRewardVideoAd? = null

    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(true)
                .setRewardName("金币")//奖励的名称
                .setRewardAmount(3)//奖励的数量
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                //可设置为空字符串
                .setUserID("")
                .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
//                .setMediaExtra("media_extra") //用户透传的信息，可不传
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadRewardVideoAd(adSlot, object : TTAdNative.RewardVideoAdListener {
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
                //mttRewardVideoAd.setShowDownLoadBar(false);
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
                        callbackRewardClose(adProviderType, listener)
                        mttRewardVideoAd = null
                    }

                    override fun onVideoComplete() {
                        "onVideoComplete".logi(TAG)
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String) {
                        "verify:$rewardVerify amount:$rewardAmount name:$rewardName".logi(TAG)
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