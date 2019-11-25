package com.rumtel.ad.helper.inter

import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
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
object TogetherAdInter : AdBase() {

    fun showAdInter(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {

        when (AdRandomUtil.getRandomAdName(interConfigStr)) {
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
            AdNameType.CSJ -> showAdInterCsj(
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
    private fun showAdInterTecentGDT(@NonNull activity: Activity, interConfigStr: String?, @NonNull adConstStr: String, @NonNull isLandscape: Boolean, @NonNull adIntersContainer: RelativeLayout, @NonNull adListener: AdListenerInter) {

        adListener.onStartRequest(AdNameType.GDT.type)

        val adListenerNative = object : NativeMediaAD.NativeMediaADListener {
            override fun onADLoaded(adList: List<NativeMediaADData>?) {
                if (adList?.isEmpty() != false) {
                    loge("${AdNameType.GDT.type}: 广点通信息流伪装插屏返回空的")
                    val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                    return
                }

                //获取一个广告
                val adItem = adList[0]

                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.prepared)}, ecpm: ${adItem.ecpm}, ecpmLevel: ${adItem.ecpmLevel}")
                adListener.onAdPrepared(AdNameType.GDT.type)

                val dm = DisplayMetrics()
                activity.windowManager.defaultDisplay.getMetrics(dm)
                //图片以16：9的宽高比展示
                //无论是横屏还是竖屏都是取小的那个长度的80%
                val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()

                val relativeLayout = RelativeLayout(activity)
                val rParams = RelativeLayout.LayoutParams(n, n * 9 / 16)
                rParams.addRule(RelativeLayout.CENTER_IN_PARENT)
                relativeLayout.layoutParams = rParams

                //广告的图片
                val imageView = ImageView(activity)
                imageView.layoutParams = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )
                imageView.scaleType = ImageView.ScaleType.FIT_XY

                //关闭按钮
                val closeParam = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                )
                closeParam.topMargin = 15
                closeParam.rightMargin = 15
                closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                val ivClose = ImageView(activity)
                ivClose.layoutParams = closeParam
                ivClose.setImageResource(R.mipmap.ad_close)
                ivClose.setOnClickListener {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.dismiss)}")
                    adIntersContainer.removeAllViews()
                    adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                    adIntersContainer.visibility = View.GONE
                    adListener.onAdDismissed()
                }

                //广点通的Logo
                val logoViewParams = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                val gdtLogoView = ImageView(activity)
                gdtLogoView.layoutParams = logoViewParams
                gdtLogoView.setImageResource(R.drawable.gdt_ad_logo)

                ILFactory.getLoader()
                    .load(activity, imageView, adItem.imgUrl, LoaderOptions(), object : LoadListener() {
                        override fun onLoadCompleted(p0: Drawable?): Boolean {
                            adItem.onExposured(adIntersContainer)

                            imageView.setOnClickListener {
                                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
                                adListener.onAdClick(AdNameType.GDT.type)
                                adItem.onClicked(it)
                            }

                            relativeLayout.addView(ivClose)
                            relativeLayout.addView(gdtLogoView)
                            adIntersContainer.setBackgroundColor(Color.parseColor("#60000000"))
                            adIntersContainer.setOnClickListener {}

                            //将容器中的所有东西删除
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

            override fun onNoAD(adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
            }

            override fun onADStatusChanged(ad: NativeMediaADData?) {}

            override fun onADError(adData: NativeMediaADData?, adError: AdError?) {
                loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
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

        val mADManager = NativeMediaAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], adListenerNative)
        mADManager.setMaxVideoDuration(60)
        mADManager.loadAD(1)


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
        TTAdSdk.getAdManager().createAdNative(activity)
            .loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
                override fun onError(errorCode: Int, errorMsg: String?) {
                    loge("${AdNameType.CSJ.type}: $errorCode : $errorMsg")
                    val newConfigStr = interConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                }

                override fun onNativeAdLoad(adList: MutableList<TTNativeAd?>?) {
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
                    imageView.layoutParams = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT
                    )
                    imageView.scaleType = ImageView.ScaleType.FIT_XY

                    //关闭按钮
                    val closeParam = RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                    )
                    closeParam.topMargin = 15
                    closeParam.rightMargin = 15
                    closeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    val ivClose = ImageView(activity)
                    ivClose.layoutParams = closeParam
                    ivClose.setImageResource(R.mipmap.ad_close)
                    ivClose.setOnClickListener {
                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                        adIntersContainer.removeAllViews()
                        adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                        adIntersContainer.visibility = View.GONE
                        adListener.onAdDismissed()
                    }

                    //广告标示
                    val logoViewParams = RelativeLayout.LayoutParams(60, 60)

                    logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)

                    val logoView = ImageView(activity)
                    logoView.layoutParams = logoViewParams
                    logoView.setImageBitmap(adItem.adLogo)

                    //不喜欢的标识, 绑定网盟dislike逻辑，有助于精准投放
                    val dislikeViewParams = RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )

                    val dislikeView = TextView(activity)
                    dislikeView.layoutParams = dislikeViewParams
                    dislikeView.setText(R.string.dislike)

                    val dislikeDialog = adItem.getDislikeDialog(activity)
                    dislikeDialog?.setDislikeInteractionCallback(object : TTAdDislike.DislikeInteractionCallback {
                        override fun onSelected(position: Int, value: String?) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                            adIntersContainer.removeAllViews()
                            adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
                            adIntersContainer.visibility = View.GONE
                            adListener.onAdDismissed()
                        }

                        override fun onCancel() {
                        }
                    })
                    dislikeView.setOnClickListener {
                        dislikeDialog?.showDislikeDialog()
                    }

                    //绑定广告view事件交互
                    val clickViewList = mutableListOf<View>()
                    clickViewList.add(imageView)
                    adItem.registerViewForInteraction(relativeLayout, clickViewList, clickViewList, dislikeView,
                        object : TTNativeAd.AdInteractionListener {
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
                                adIntersContainer.setBackgroundColor(Color.parseColor("#00000000"))
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

                    ILFactory.getLoader()
                        .load(activity, imageView, ttImage.imageUrl, LoaderOptions(), object : LoadListener() {
                            override fun onLoadCompleted(p0: Drawable?): Boolean {
                                relativeLayout.addView(ivClose)
                                relativeLayout.addView(logoView)
                                adIntersContainer.setBackgroundColor(Color.parseColor("#60000000"))
                                adIntersContainer.setOnClickListener {}
                                //将容器中的所有东西删除
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

    interface AdListenerInter {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }
}