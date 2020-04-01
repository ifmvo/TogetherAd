package com.rumtel.ad.helper.mid

import android.app.Activity
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.progress.LoaderOptions
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.*
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.rumtel.ad.AdLogoView
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge

/* 
 * (●ﾟωﾟ●) 用于界面中间插一个广告
 * 
 * Created by Matthew Chen on 2020-03-31.
 */
object TogetherAdMid : AdBase() {

    private var stop = false

    fun showAdMid(@NonNull activity: Activity, midConfigStr: String?, @NonNull adConstStr: String, @NonNull adContainer: ViewGroup, @NonNull adListener: AdListenerMid) {

        stop = false

        when (AdRandomUtil.getRandomAdName(midConfigStr)) {
            AdNameType.BAIDU -> {
                showAdMidBaiduMob(activity, midConfigStr, adConstStr, adContainer, adListener)
            }
            AdNameType.GDT -> {
                showAdMidTecentGDT(activity, midConfigStr, adConstStr, adContainer, adListener)
            }
            AdNameType.CSJ -> {
                showAdMidCsj(activity, midConfigStr, adConstStr, adContainer, adListener)
            }
            else -> {
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
            }
        }
    }

    private var adItem: NativeUnifiedADData? = null
    private fun showAdMidTecentGDT(@NonNull activity: Activity, midConfigStr: String?, @NonNull adConstStr: String, @NonNull adContainer: ViewGroup, @NonNull adListener: AdListenerMid) {
        adListener.onStartRequest(AdNameType.GDT.type)
        val listener = object : NativeADUnifiedListener {
            override fun onADLoaded(adList: List<NativeUnifiedADData>?) {

                if (stop) {
                    return
                }

                if (adList?.isEmpty() != false) {
                    loge("${AdNameType.GDT.type}: 广点通信息流伪装MID返回空的")
                    val newConfigStr = midConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    showAdMid(activity, newConfigStr, adConstStr, adContainer, adListener)
                    return
                }

                //获取一个广告
                adItem = adList[0]

                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.GDT.type)

                val rootView = View.inflate(activity, R.layout.view_ad_mid_gdt, null)
                val nativeAdContainer = rootView.findViewById<NativeAdContainer>(R.id.native_ad_container)
                val ivImg = rootView.findViewById<ImageView>(R.id.iv_img)
                val mediaView = rootView.findViewById<MediaView>(R.id.media_view)
                val tvTitle = rootView.findViewById<TextView>(R.id.tv_title)
                val tvDesc = rootView.findViewById<TextView>(R.id.tv_desc)

                logd("${AdNameType.GDT.type}: 原生类型： ${adItem?.adPatternType}")
                tvTitle.text = adItem?.title
                tvDesc.text = adItem?.desc
                when (adItem?.adPatternType) {
                    AdPatternType.NATIVE_2IMAGE_2TEXT, AdPatternType.NATIVE_3IMAGE -> {
                        mediaView.visibility = View.GONE
                        ivImg.visibility = View.VISIBLE
                        ILFactory.getLoader().load(activity, ivImg, adItem?.imgUrl, LoaderOptions())
                    }
                    AdPatternType.NATIVE_VIDEO -> {
                        mediaView.visibility = View.VISIBLE
                        ivImg.visibility = View.GONE
                    }
                }
                val clickableViews = arrayListOf<View>()
                clickableViews.add(nativeAdContainer)
                clickableViews.add(ivImg)
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

                adContainer.visibility = View.VISIBLE
                if (adContainer.childCount > 0) {
                    adContainer.removeAllViews()
                }

                adContainer.addView(rootView)
            }

            override fun onNoAD(adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                if (stop) {
                    return
                }
                val newConfigStr = midConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdMid(activity, newConfigStr, adConstStr, adContainer, adListener)
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

    private fun showAdMidBaiduMob(@NonNull activity: Activity, midConfigStr: String?, @NonNull adConstStr: String, @NonNull adContainer: ViewGroup, @NonNull adListener: AdListenerMid) {

        adListener.onStartRequest(AdNameType.BAIDU.type)
        val baidu = BaiduNative(activity, TogetherAd.idMapBaidu[adConstStr], object : BaiduNative.BaiduNativeNetworkListener {

            override fun onNativeLoad(list: List<NativeResponse>?) {

                if (list.isNullOrEmpty()) {
                    loge("${AdNameType.BAIDU.type}: 返回的广告是空的")
                    val newListConfig = midConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                    showAdMid(activity, newListConfig, adConstStr, adContainer, adListener)
                    return
                }

                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.BAIDU.type)

                //获取一个广告
                val adItem = list[0]

                val rootView = View.inflate(activity, R.layout.view_ad_mid_baidu, null)
                val ivImg = rootView.findViewById<ImageView>(R.id.iv_img)
                val adLogoView = rootView.findViewById<AdLogoView>(R.id.ad_logo_view)
                val tvTitle = rootView.findViewById<TextView>(R.id.tv_title)
                val tvDesc = rootView.findViewById<TextView>(R.id.tv_desc)

                tvTitle.text = adItem.title
                tvDesc.text = adItem.desc
                adLogoView.setAdLogoType(AdNameType.BAIDU, adItem)

                adContainer.visibility = View.VISIBLE
                if (adContainer.childCount > 0) {
                    adContainer.removeAllViews()
                }
                ILFactory.getLoader().load(activity, ivImg, adItem.imageUrl, LoaderOptions())
                adContainer.addView(rootView)
                adItem.recordImpression(rootView)
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.exposure)}")
                rootView.setOnClickListener {
                    adItem.handleClick(it)
                    logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                    adListener.onAdClick(AdNameType.BAIDU.type)
                }
            }

            override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                val newListConfig = midConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdMid(activity, newListConfig, adConstStr, adContainer, adListener)
                loge("${AdNameType.BAIDU.type}: nativeErrorCode: $nativeErrorCode")
            }
        })
        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder().build()
        baidu.makeRequest(requestParameters)
    }

    private fun showAdMidCsj(@NonNull activity: Activity, midConfigStr: String?, @NonNull adConstStr: String, @NonNull adContainer: ViewGroup, @NonNull adListener: AdListenerMid) {
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
                val newConfigStr = midConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                showAdMid(activity, newConfigStr, adConstStr, adContainer, adListener)
            }

            override fun onNativeAdLoad(adList: MutableList<TTNativeAd?>?) {
                if (stop) {
                    return
                }
                if (adList.isNullOrEmpty() || adList[0] == null) {
                    loge("${AdNameType.CSJ.type}: 穿山甲返回的广告是 null")
                    val newConfigStr = midConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdMid(activity, newConfigStr, adConstStr, adContainer, adListener)
                    return
                }

                logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.CSJ.type)

                //获取一个广告
                val adItem = adList[0]!!

                val rootView = View.inflate(activity, R.layout.view_ad_mid_csj, null) as ViewGroup
                val ivImg = rootView.findViewById<ImageView>(R.id.iv_img)
                val adLogoView = rootView.findViewById<AdLogoView>(R.id.ad_logo_view)
                val tvTitle = rootView.findViewById<TextView>(R.id.tv_title)
                val tvDesc = rootView.findViewById<TextView>(R.id.tv_desc)

                //不喜欢的标识, 绑定网盟dislike逻辑，有助于精准投放
                val dislikeViewParams = RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

                val dislikeView = TextView(activity)
                dislikeView.layoutParams = dislikeViewParams
                dislikeView.setText(R.string.dislike)

                val dislikeDialog = adItem.getDislikeDialog(activity)
                dislikeDialog?.setDislikeInteractionCallback(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                        adContainer.removeAllViews()
                        adContainer.visibility = View.GONE
                        adListener.onAdDismissed()
                    }

                    override fun onCancel() {}
                })
                dislikeView.setOnClickListener {
                    dislikeDialog?.showDislikeDialog()
                }

                //绑定广告view事件交互
                val clickViewList = mutableListOf<View>()
                clickViewList.add(rootView)
                clickViewList.add(ivImg)
                adItem.registerViewForInteraction(rootView, clickViewList, clickViewList, dislikeView, object : TTNativeAd.AdInteractionListener {
                    override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                        //clickViewList 和 creativeClickList 一样就只会回调 onAdCreativeClick
                    }

                    override fun onAdShow(p0: TTNativeAd?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.exposure)}")
                    }

                    override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.clicked)}")
                        adListener.onAdClick(AdNameType.CSJ.type)
                    }
                })

                //加载ad 图片资源
                val imageList = adItem.imageList
                if (imageList.isNullOrEmpty() || imageList[0] == null) {
                    loge("${AdNameType.CSJ.type}: 广告里面的图片是null")
                    val newConfigStr = midConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdMid(activity, newConfigStr, adConstStr, adContainer, adListener)
                    return
                }

                val ttImage = imageList[0]

                tvTitle.text = adItem.title
                tvDesc.text = adItem.description
                adLogoView.setAdLogoType(AdNameType.CSJ, adItem)
                ILFactory.getLoader().load(activity, ivImg, ttImage.imageUrl, LoaderOptions())

                adContainer.visibility = View.VISIBLE
                if (adContainer.childCount > 0) {
                    adContainer.removeAllViews()
                }

                adContainer.addView(rootView)
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

    interface AdListenerMid {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }

}