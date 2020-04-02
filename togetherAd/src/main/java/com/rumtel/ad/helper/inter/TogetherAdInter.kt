package com.rumtel.ad.helper.inter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.annotation.NonNull
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.baidu.mobads.AdSize
import com.baidu.mobads.InterstitialAd
import com.baidu.mobads.InterstitialAdListener
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.LoadListener
import com.ifmvo.imageloader.progress.LoaderOptions
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.*
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge


/*
 * (●ﾟωﾟ●) 插屏的广告
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
object TogetherAdInter : AdBase() {

    private var stop = false

    fun showAdInter(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {

        stop = false

        when (AdRandomUtil.getRandomAdName(interConfigStr)) {
            AdNameType.BAIDU -> {
                showAdInterBaiduMob(activity, interConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }
            AdNameType.GDT -> {
                showAdInterTecentGDT(activity, interConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }
            AdNameType.CSJ -> {
                showAdInterCsj(activity, interConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }
            else -> {
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
            }
        }
    }

    private var adItem: NativeUnifiedADData? = null
    private fun showAdInterTecentGDT(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {
        adListener.onStartRequest(AdNameType.GDT.type)
        val listener = object : NativeADUnifiedListener {
            override fun onADLoaded(adList: List<NativeUnifiedADData>?) {

                if (stop) {
                    return
                }

                if (adList?.isEmpty() != false) {
                    loge("${AdNameType.GDT.type}: 广点通信息流伪装插屏返回空的")
                    val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                    return
                }

                //获取一个广告
                adItem = adList[0]

                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.GDT.type)

                val dm = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(dm)
                //图片以16：9的宽高比展示
                //无论是横屏还是竖屏都是取小的那个长度的80%
                val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()

                val nativeAdContainer = View.inflate(activity, R.layout.view_inter_gdt, null) as NativeAdContainer
                val ivClose = nativeAdContainer.findViewById<View>(R.id.iv_close)
                val ivImage = nativeAdContainer.findViewById<ImageView>(R.id.iv_image)
                val mediaView = nativeAdContainer.findViewById<MediaView>(R.id.media_view)

                ivClose.setOnClickListener {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.dismiss)}")
                    adIntersContainer.removeAllViews()
                    adIntersContainer.visibility = View.GONE
                    adListener.onAdDismissed()
                    when (adItem?.adPatternType) {
                        AdPatternType.NATIVE_VIDEO -> {
                            adItem?.stopVideo()
                        }
                    }
                    adItem?.destroy()
                    adItem = null
                }

                ILFactory.getLoader().load(activity, ivImage, adItem?.imgUrl, LoaderOptions())
                logd("${AdNameType.GDT.type}: 原生类型： ${adItem?.adPatternType}")
                when (adItem?.adPatternType) {
                    AdPatternType.NATIVE_2IMAGE_2TEXT, AdPatternType.NATIVE_3IMAGE -> {
                        mediaView.visibility = View.GONE
                        ivImage.visibility = View.VISIBLE
                        ILFactory.getLoader().load(activity, ivImage, adItem?.imgUrl, LoaderOptions().skipCache())
                    }
                    AdPatternType.NATIVE_VIDEO -> {
                        mediaView.visibility = View.VISIBLE
                        ivImage.visibility = View.GONE
                    }
                }
                val clickableViews = arrayListOf<View>()
                clickableViews.add(nativeAdContainer)
                clickableViews.add(ivImage)
                clickableViews.add(mediaView)
                adItem?.bindAdToView(activity, nativeAdContainer, null, clickableViews)
                adItem?.setNativeAdEventListener(object : NativeADEventListener {
                    override fun onADStatusChanged() {}

                    override fun onADError(error: AdError?) {
                        logd("${AdNameType.GDT.type}: ${error?.errorCode} ${error?.errorMsg}")
                    }

                    override fun onADClicked() {
                        logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
                        adListener.onAdClick(AdNameType.GDT.type)
                    }

                    override fun onADExposed() {
                        logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
                    }
                })
                when (adItem?.adPatternType) {
                    AdPatternType.NATIVE_VIDEO -> {
                        val videoOption = VideoOption.Builder().setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build()
                        adItem?.bindMediaView(mediaView, videoOption, object : NativeADMediaListener {
                            override fun onVideoInit() {}
                            override fun onVideoStop() {}
                            override fun onVideoPause() {}
                            override fun onVideoStart() {}
                            override fun onVideoError(p0: AdError?) {}
                            override fun onVideoCompleted() {}
                            override fun onVideoLoading() {}
                            override fun onVideoReady() {}
                            override fun onVideoLoaded(p0: Int) {}
                            override fun onVideoClicked() {}
                            override fun onVideoResume() {}
                        })
                        adItem?.startVideo()
                    }
                }

                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }

                val adLayoutParams = RelativeLayout.LayoutParams(n, n * 9 / 16)
                adLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                adIntersContainer.addView(nativeAdContainer, adLayoutParams)
            }

            override fun onNoAD(adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                if (stop) {
                    return
                }
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }
        }

        val mAdManager = NativeUnifiedAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], listener)
        //有效值就是 5-60
        mAdManager.setMaxVideoDuration(60)
        mAdManager.setMinVideoDuration(5)
        mAdManager.setVideoPlayPolicy(VideoOption.VideoPlayPolicy.AUTO) // 本次拉回的视频广告，在用户看来是否为自动播放的
        mAdManager.setVideoADContainerRender(VideoOption.VideoADContainerRender.SDK) // 视频播放前，用户看到的广告容器是由SDK渲染的
        mAdManager.loadData(1)
    }

    private fun showAdInterBaiduMob(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {

        adListener.onStartRequest(AdNameType.BAIDU.type)

        val interAd = InterstitialAd(activity, AdSize.InterstitialForVideoPausePlay, TogetherAd.idMapBaidu[adConstStr])

        interAd.setListener(object : InterstitialAdListener {
            override fun onAdReady() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.show)}")
                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }
                adIntersContainer.setBackgroundColor(Color.parseColor("#60000000"))
                interAd.showAdInParentForVideoApp(activity, adIntersContainer)
            }

            override fun onAdPresent() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
                adIntersContainer.setOnClickListener {
                    adIntersContainer.removeAllViews()
                    adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                    adIntersContainer.visibility = View.GONE
                }
                adListener.onAdPrepared(AdNameType.BAIDU.type)
            }

            override fun onAdClick(interstitialAd: InterstitialAd) {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                adListener.onAdClick(AdNameType.BAIDU.type)
            }

            override fun onAdDismissed() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                adIntersContainer.removeAllViews()
                adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                adIntersContainer.visibility = View.GONE
                adListener.onAdDismissed()
            }

            override fun onAdFailed(s: String) {
                loge("${AdNameType.BAIDU.type}: $s")
                adIntersContainer.removeAllViews()
                adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                adIntersContainer.visibility = View.GONE
                val newConfigStr = interConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }
        })
        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)

        val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()
        interAd.loadAdForVideoApp(n, (n * 0.8).toInt())

        adIntersContainer.postDelayed({
            interAd.loadAdForVideoApp(n, (n * 0.8).toInt())
        }, 1000)

    }

    private fun showAdInterCsj(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {
        adListener.onStartRequest(AdNameType.CSJ.type)

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        //图片以16：9的宽高比展示
        //无论是横屏还是竖屏都是取小的那个长度的80%
        val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAd.idMapCsj[adConstStr])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(n, n * 9 / 16)
                .setNativeAdType(AdSlot.TYPE_INTERACTION_AD)//请求原生广告时候，请务必调用该方法，设置参数为TYPE_BANNER或TYPE_INTERACTION_AD
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onError(errorCode: Int, errorMsg: String?) {
                loge("${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                if (stop) {
                    return
                }
                val newConfigStr = interConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }

            override fun onNativeAdLoad(adList: MutableList<TTNativeAd?>?) {
                if (stop) {
                    return
                }
                if (adList.isNullOrEmpty() || adList[0] == null) {
                    loge("${AdNameType.CSJ.type}: 穿山甲返回的广告是 null")
                    val newConfigStr = interConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                    return
                }

                logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.CSJ.type)

                //获取一个广告
                val adItem = adList[0]!!

                val relativeLayout = RelativeLayout(activity)
                val rParams = RelativeLayout.LayoutParams(n, n * 9 / 16)
                rParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                relativeLayout.layoutParams = rParams

                //广告的图片
                val imageView = ImageView(activity)
                imageView.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                //关闭按钮
                val closeParam = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT)
                closeParam.topMargin = 15
                closeParam.rightMargin = 15
                closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                val ivClose = ImageView(activity)
                ivClose.layoutParams = closeParam
                ivClose.setImageResource(R.mipmap.ad_close)
                ivClose.setOnClickListener {
                    logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                    adIntersContainer.removeAllViews()
                    adIntersContainer.visibility = View.GONE
                    adListener.onAdDismissed()
                }

                //广告标示
                val logoViewParams = RelativeLayout.LayoutParams(135, 45)

                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                val logoView = ImageView(activity)
                logoView.layoutParams = logoViewParams
                logoView.setImageResource(R.drawable.ic_ad_logo_csj)
//                logoView.setImageBitmap(adItem.adLogo)

                //不喜欢的标识, 绑定网盟dislike逻辑，有助于精准投放
                val dislikeViewParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                val dislikeView = TextView(activity)
                dislikeView.layoutParams = dislikeViewParams
                dislikeView.setText(R.string.dislike)

                val dislikeDialog = adItem.getDislikeDialog(activity)
                dislikeDialog?.setDislikeInteractionCallback(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                        adIntersContainer.removeAllViews()
                        adIntersContainer.visibility = View.GONE
                        adListener.onAdDismissed()
                    }

                    override fun onCancel() {}
                })
                dislikeView.setOnClickListener {
                    dislikeDialog?.showDislikeDialog()
                }

                //绑定广告view事件交互
                val clickViewList = mutableListOf<View>()
                clickViewList.add(imageView)
                adItem.registerViewForInteraction(relativeLayout, clickViewList, clickViewList, dislikeView, object : TTNativeAd.AdInteractionListener {
                    override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                        //clickViewList 和 creativeClickList 一样就只会回调 onAdCreativeClick
                    }

                    override fun onAdShow(p0: TTNativeAd?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.exposure)}")
                    }

                    override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.clicked)}")
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                        adIntersContainer.removeAllViews()
                        adIntersContainer.visibility = View.GONE
                        adListener.onAdClick(AdNameType.CSJ.type)
                        adListener.onAdDismissed()
                    }
                })

                //加载ad 图片资源
                val imageList = adItem.imageList
                if (imageList.isNullOrEmpty() || imageList[0] == null) {
                    loge("${AdNameType.CSJ.type}: 广告里面的图片是null")
                    val newConfigStr = interConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                    return
                }

                val ttImage = imageList[0]

                ILFactory.getLoader().load(activity, imageView, ttImage.imageUrl, LoaderOptions(), object : LoadListener() {
                    override fun onLoadCompleted(p0: Drawable?): Boolean {
                        relativeLayout.addView(ivClose)
                        relativeLayout.addView(logoView)
                        return true
                    }
                })

                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }

                relativeLayout.addView(imageView)
                adIntersContainer.addView(relativeLayout)
            }
        })
    }

    fun resume() {
        adItem?.resume()
        when (adItem?.adPatternType) {
            AdPatternType.NATIVE_VIDEO -> {
                adItem?.resumeVideo()
            }
        }
    }

    fun pause() {
        when (adItem?.adPatternType) {
            AdPatternType.NATIVE_VIDEO -> {
                adItem?.pauseVideo()
            }
        }
    }

    fun destroy() {
        stop = true
    }

    interface AdListenerInter {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }
}