package com.rumtel.ad.helper.inter

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.baidu.mobads.AdSize
import com.baidu.mobads.InterstitialAd
import com.baidu.mobads.InterstitialAdListener
import com.iflytek.voiceads.IFLYNativeAd
import com.iflytek.voiceads.IFLYNativeListener
import com.iflytek.voiceads.NativeADDataRef
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.LoadListener
import com.ifmvo.imageloader.progress.LoaderOptions
import com.qq.e.ads.nativ.NativeMediaAD
import com.qq.e.ads.nativ.NativeMediaADData
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
object TogetherAdInter : AdBase {

    fun showAdInter(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {
//        var newConfigStr = interConfigStr

//        //目前这个版本先这样写，横屏下广点通大概率展示不出来的问题
//        if (isLandscape && !TextUtils.isEmpty(interConfigStr)) {
//            newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
//        }

        val randomAdName = AdRandomUtil.getRandomAdName(interConfigStr)
        when (randomAdName) {
            AdNameType.BAIDU -> showAdInterBaiduMob(
                activity,
                interConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            AdNameType.GDT -> showAdInterTecentGDT(
                activity,
                interConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            AdNameType.XUNFEI -> showAdInterIFLY(
                activity,
                interConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            else -> {
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
            }
        }
    }

    /**
     * onNoAD(AdError error)	广告加载失败，error对象包含了错误码和错误信息，错误码的详细内容可以参考文档第5章
     * onADReceive()	插屏广告加载完毕，此回调后才可以调用show方法
     * onADOpened()	插屏广告展开时回调
     * onADExposure()	插屏广告曝光时回调
     * onADClicked()	插屏广告点击时回调
     * onADClosed()	插屏广告关闭时回调
     * onADLeftApplication()	插屏广告点击离开应用时回调
     */
    private fun showAdInterTecentGDT(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {

        adListener.onStartRequest(AdNameType.GDT.type)

        val adListenerNative = object : NativeMediaAD.NativeMediaADListener {
            override fun onADLoaded(adList: List<NativeMediaADData>?) {
                if (adList?.isEmpty() != false) {
                    loge("${AdNameType.GDT.type}: 广点通信息流伪装插屏返回空的")
                    val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    showAdInter(
                        activity,
                        newConfigStr,
                        adConstStr,
                        isLandscape,
                        adIntersContainer,
                        adListener
                    )
                    return
                }

                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.GDT.type)
                //将容器中的所有东西删除
                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }

                //获取一个广告
                val adItem = adList[0]
                val relativeLayout = RelativeLayout(activity)
                val dm = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(dm)
                //图片以16：9的宽高比展示
                //无论是横屏还是竖屏都是取小的那个长度的80%
                val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()

                val rParams = RelativeLayout.LayoutParams(n, n * 9 / 16)
                rParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                relativeLayout.layoutParams = rParams

                val imageView = ImageView(activity)
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                ILFactory.getLoader()
                    .load(activity, imageView, adItem.imgUrl, LoaderOptions(), object : LoadListener() {
                        override fun onLoadCompleted(p0: Drawable?): Boolean {
                            adItem.onExposured(adIntersContainer)
                            val closeParam = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                            closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            val ivClose = ImageView(activity)
                            ivClose.layoutParams = closeParam
                            ivClose.setImageResource(R.mipmap.ad_close)
                            ivClose.setOnClickListener {
                                adIntersContainer.visibility = View.GONE
                            }
                            imageView.setOnClickListener {
                                adListener.onAdClick(AdNameType.GDT.type)
                                adItem.onClicked(it)
                            }
                            relativeLayout.addView(ivClose)

                            val logoViewParams = RelativeLayout.LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                            val gdtLogoView = ImageView(activity)
                            gdtLogoView.layoutParams = logoViewParams
                            gdtLogoView.setImageResource(R.drawable.gdt_ad_logo)

                            relativeLayout.addView(gdtLogoView)

                            return true
                        }
                    })
                relativeLayout.addView(imageView)
                adIntersContainer.addView(relativeLayout)
            }

            override fun onNoAD(adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(
                    activity,
                    newConfigStr,
                    adConstStr,
                    isLandscape,
                    adIntersContainer,
                    adListener
                )
            }

            override fun onADStatusChanged(ad: NativeMediaADData?) {}

            override fun onADError(adData: NativeMediaADData?, adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(
                    activity,
                    newConfigStr,
                    adConstStr,
                    isLandscape,
                    adIntersContainer,
                    adListener
                )
            }

            override fun onADVideoLoaded(adData: NativeMediaADData?) {
                logd("${AdNameType.GDT.type}: 视频素材加载完成")
            }

            override fun onADExposure(adData: NativeMediaADData?) {
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
            }

            override fun onADClicked(adData: NativeMediaADData?) {
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
            }
        }

        val mADManager =
            NativeMediaAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], adListenerNative)
        mADManager.loadAD(1)


//        adListener.onStartRequest(AdNameType.GDT.type)
//        val iad = InterstitialAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr])
//        iad.setADListener(object : InterstitialADListener {
//            override fun onADReceive() {
//                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.show)}")
//                iad.show()
//            }
//
//            override fun onNoAD(error: AdError) {
////                loge("${AdNameType.GDT.type}: ${error.errorCode}, ${error.errorMsg}")
////                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
////                showAdInter(
////                    activity,
////                    newConfigStr,
////                    adConstStr,
////                    isLandscape,
////                    adIntersContainer,
////                    adListener
////                )
//            }
//
//            override fun onADOpened() {}
//
//            override fun onADExposure() {
//                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
//                adListener.onAdPrepared(AdNameType.GDT.type)
//            }
//
//            override fun onADClicked() {
//                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
//                adListener.onAdClick(AdNameType.GDT.type)
//            }
//
//            override fun onADLeftApplication() {}
//
//            override fun onADClosed() {}
//        })
//
//        iad.loadAD()
    }

    private fun showAdInterBaiduMob(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {

        adListener.onStartRequest(AdNameType.BAIDU.type)
        adIntersContainer.setOnClickListener { adIntersContainer.visibility = View.GONE }
        adIntersContainer.visibility = View.GONE

        val interAd = InterstitialAd(activity, AdSize.InterstitialForVideoPausePlay, TogetherAd.idMapBaidu[adConstStr])

        interAd.setListener(object : InterstitialAdListener {
            override fun onAdReady() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.show)}")
                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }
                interAd.showAdInParentForVideoApp(activity, adIntersContainer)
            }

            override fun onAdPresent() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.BAIDU.type)
            }

            override fun onAdClick(interstitialAd: InterstitialAd) {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                adListener.onAdClick(AdNameType.BAIDU.type)
            }

            override fun onAdDismissed() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                adIntersContainer.visibility = View.GONE
                adListener.onAdDismissed()
            }

            override fun onAdFailed(s: String) {
                loge("${AdNameType.BAIDU.type}: $s")
                val newConfigStr = interConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdInter(
                    activity,
                    newConfigStr,
                    adConstStr,
                    isLandscape,
                    adIntersContainer,
                    adListener
                )
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

    private fun showAdInterIFLY(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {
        adListener.onStartRequest(AdNameType.XUNFEI.type)

        val mListener = object : IFLYNativeListener {
            override fun onADLoaded(list: MutableList<NativeADDataRef>?) {
                if (list?.isEmpty() != false) {
                    loge("${AdNameType.XUNFEI.type}: 科大讯飞信息流伪装插屏返回空的")
                    val newConfigStr = interConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                    showAdInter(
                        activity,
                        newConfigStr,
                        adConstStr,
                        isLandscape,
                        adIntersContainer,
                        adListener
                    )
                    return
                }
                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.XUNFEI.type)
                adIntersContainer.visibility = View.VISIBLE
                if (adIntersContainer.childCount > 0) {
                    adIntersContainer.removeAllViews()
                }

                val adItem = list[0]

                val relativeLayout = RelativeLayout(activity)
                val dm = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(dm)
                val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()
                relativeLayout.layoutParams = RelativeLayout.LayoutParams(n, n * 2 / 3)

                val imageView = ImageView(activity)
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                ILFactory.getLoader()
                    .load(activity, imageView, adItem.image, LoaderOptions(), object : LoadListener() {
                        override fun onLoadCompleted(p0: Drawable?): Boolean {
                            if (adItem.onExposured(imageView)) {
                                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.exposure)}")
                            }
                            val closeParam = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            )
                            closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                            val ivClose = ImageView(activity)
                            ivClose.layoutParams = closeParam
                            ivClose.setImageResource(R.mipmap.ad_close)
                            ivClose.setOnClickListener {
                                adIntersContainer.visibility = View.GONE
                            }
                            imageView.setOnClickListener {
                                if (adItem.onClicked(imageView)) {
                                    logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.clicked)}")
                                    adListener.onAdClick(AdNameType.XUNFEI.type)
                                }
                            }
                            relativeLayout.addView(ivClose)
                            return true
                        }
                    })

                relativeLayout.addView(imageView)
                adIntersContainer.addView(relativeLayout)
            }


            override fun onAdFailed(adError: com.iflytek.voiceads.AdError) {
                loge("${AdNameType.XUNFEI.type}: ${adError.errorCode}, ${adError.errorDescription}")
                val newConfigStr = interConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                showAdInter(
                    activity,
                    newConfigStr,
                    adConstStr,
                    isLandscape,
                    adIntersContainer,
                    adListener
                )
            }

            override fun onConfirm() {

            }

            override fun onCancel() {

            }
        }

        val nativeAd = IFLYNativeAd(activity, TogetherAd.idMapXunFei[adConstStr], mListener)
        nativeAd.loadAd(1)


//        adListener.onStartRequest(AdNameType.XUNFEI.type)
//        //创建插屏广告:adId:开发者在广告平台(http://www.voiceads.cn/)申请的广告位 ID
//        val interstitialAd = IFLYInterstitialAd.createInterstitialAd(activity, TogetherAd.idMapXunFei[adConstStr])
//        //点击手机后退键，是否销毁广告:"true":销毁，"false":不销毁，默认销毁 interstitialAd.setParameter(AdKeys.BACK_KEY_ENABLE, "true");
//        //设置广告尺寸
//        if (interstitialAd == null) {
//            loge("${AdNameType.XUNFEI.type}: 科大讯飞 interstitialAd 是 null")
//            val newConfigStr = interConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
//            showAdInter(
//                activity,
//                newConfigStr,
//                adConstStr,
//                isLandscape,
//                adIntersContainer,
//                adListener
//            )
//            return
//        }
//        interstitialAd.setAdSize(IFLYAdSize.INTERSTITIAL)

//        // 添加监听器，请求广告
//        val mAdListener = object : IFLYAdListener {
//            override fun onConfirm() {}
//
//            override fun onCancel() {}
//
//            override fun onAdReceive() {
//                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.prepared)}")
//                adListener.onAdPrepared(AdNameType.XUNFEI.type)
//                interstitialAd.showAd()
//            }

//            override fun onAdFailed(adError: com.iflytek.voiceads.AdError) {
//                loge("${AdNameType.XUNFEI.type}: ${adError.errorCode}, ${adError.errorDescription}")
//                val newConfigStr = interConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
//                showAdInter(
//                    activity,
//                    newConfigStr,
//                    adConstStr,
//                    isLandscape,
//                    adIntersContainer,
//                    adListener
//                )
//            }

//            override fun onAdClick() {
//                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.clicked)}")
//                adListener.onAdClick(AdNameType.XUNFEI.type)
//            }

//            override fun onAdClose() {}

//            override fun onAdExposure() {
//                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.exposure)}")
//            }
//        }
//        interstitialAd.loadAd(mAdListener)
    }

    interface AdListenerInter {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }
}