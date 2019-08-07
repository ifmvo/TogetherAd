package com.rumtel.ad.helper.banner

import android.app.Activity
import android.graphics.drawable.Drawable
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.LoadListener
import com.ifmvo.imageloader.progress.LoaderOptions
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge


/*
 * (●ﾟωﾟ●) 信息流的广告
 * 
 * Created by Matthew_Chen on 2018/12/25.
 */
object TogetherAdBanner2 : AdBase() {

    fun requestBanner(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adContainer: FrameLayout,
        @NonNull adListener: AdListenerList
    ) {

        when (AdRandomUtil.getRandomAdName(listConfigStr)) {
            AdNameType.BAIDU -> requestBannerBaidu(activity, listConfigStr, adConstStr, adContainer, adListener)
            AdNameType.GDT -> requestBannerGDT(activity, listConfigStr, adConstStr, adContainer, adListener)
            AdNameType.CSJ -> requestBannerCsj(activity, listConfigStr, adConstStr, adContainer, adListener)
            else -> {
                activity.runOnUiThread {
                    adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                }
                loge(activity.getString(R.string.all_ad_error))
            }
        }
    }

    private fun requestBannerBaidu(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adContainer: FrameLayout,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.BAIDU.type)
        val baidu = BaiduNative(
            activity,
            TogetherAd.idMapBaidu[adConstStr],
            object : BaiduNative.BaiduNativeNetworkListener {

                override fun onNativeLoad(list: List<NativeResponse>?) {

                    if (list.isNullOrEmpty()) {
                        loge("${AdNameType.BAIDU.type}: 返回的广告是空的")
                        val newListConfig = listConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                        requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                        return
                    }

                    logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
                    adListener.onAdPrepared(AdNameType.BAIDU.type)

                    //获取一个广告
                    val adItem = list[0]

                    val superView = View.inflate(activity, R.layout.layout_banner_view, null)
                    val ivImage = superView.findViewById<ImageView>(R.id.iv_img)
                    val ivClose = superView.findViewById<ImageView>(R.id.iv_close)
                    val tvTitle = superView.findViewById<TextView>(R.id.tv_title)
                    val tvDesc = superView.findViewById<TextView>(R.id.tv_desc)

                    tvTitle.text = adItem.title
                    tvDesc.text = adItem.desc

                    adContainer.visibility = View.VISIBLE
                    if (adContainer.childCount > 0) {
                        adContainer.removeAllViews()
                    }

                    val dm = DisplayMetrics()
                    activity.windowManager.defaultDisplay.getMetrics(dm)
                    val w = dm.widthPixels / 4
                    val h = w * 9 / 16
                    val layoutParams = adContainer.layoutParams
                    layoutParams.height = h

                    val ivLayoutParams = ivImage.layoutParams
                    ivLayoutParams.width = w

                    ILFactory.getLoader()
                        .load(activity, ivImage, adItem.iconUrl, LoaderOptions(), object : LoadListener() {
                            override fun onLoadCompleted(p0: Drawable?): Boolean {
                                return true
                            }
                        })
                    adContainer.addView(superView)
                    adItem.recordImpression(superView)
                    superView.setOnClickListener {
                        adItem.handleClick(it)
                        logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                        adListener.onAdClick(AdNameType.BAIDU.type)
                    }

                    ivClose.setOnClickListener {
                        logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                        adContainer.removeAllViews()
                        adContainer.visibility = View.GONE
                        adListener.onAdDismissed()
                    }

                }

                override fun onNativeFail(nativeErrorCode: NativeErrorCode) {

                    val newListConfig = listConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                    requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                    loge("${AdNameType.BAIDU.type}: nativeErrorCode: $nativeErrorCode")
                }
            })
        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder()
            .build()

        baidu.makeRequest(requestParameters)
    }

    private fun requestBannerCsj(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adContainer: FrameLayout,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.CSJ.type)

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
            .setCodeId(TogetherAd.idMapCsj[adConstStr]) //广告位id
            .setSupportDeepLink(true)
            .setAdCount(1) //请求广告数量为1到3条
            .setExpressViewAcceptedSize(wDp, hDp) //期望模板广告view的size,单位dp
            .setImageAcceptedSize(350, 350)//这个参数设置即可，不影响模板广告的size
            .build()

        TTAdSdk.getAdManager().createAdNative(activity)
            .loadBannerExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
                override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                    if (ads?.isEmpty() != false) {
                        loge("${AdNameType.CSJ.type}: 返回的广告是空的")
                        val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                        requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                        return
                    }

                    val mTTAd = ads[0]
                    mTTAd.setSlideIntervalTime(30 * 1000)

                    mTTAd.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(p0: View?, type: Int) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.clicked)}")
                            adListener.onAdClick(AdNameType.CSJ.type)
                        }

                        override fun onAdShow(p0: View?, type: Int) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.exposure)}")
                        }

                        override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                            adContainer.visibility = View.VISIBLE
                            if (adContainer.childCount > 0) {
                                adContainer.removeAllViews()
                            }
                            adContainer.addView(view)
                        }

                        override fun onRenderFail(p0: View?, msg: String?, code: Int) {
                            loge("${AdNameType.CSJ.type}: onRenderFail: code:$code, msg: $msg")
                            val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                            activity.runOnUiThread {
                                requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                            }
                        }
                    })

                    mTTAd.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
                        override fun onSelected(p0: Int, p1: String?) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                            adContainer.removeAllViews()
                            adContainer.visibility = View.GONE
                            adListener.onAdDismissed()
                        }

                        override fun onCancel() {}
                    })

                    mTTAd.render()
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    loge("${AdNameType.CSJ.type}: errorCode: $errorCode, errorMsg: $errorMsg")
                    val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                }
            })
    }

    private var bannerView: UnifiedBannerView? = null
    private fun requestBannerGDT(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adContainer: FrameLayout,
        @NonNull adListener: AdListenerList
    ) {

        adListener.onStartRequest(AdNameType.GDT.type)
        bannerView = UnifiedBannerView(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr],
            object : UnifiedBannerADListener {
                override fun onADCloseOverlay() {
                }

                override fun onADExposure() {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
                }

                override fun onADClosed() {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.dismiss)}")
                    adListener.onAdDismissed()
                    bannerView?.destroy()
                    bannerView = null
                }

                override fun onADLeftApplication() {
                }

                override fun onADOpenOverlay() {
                }

                override fun onNoAD(adError: AdError?) {
                    loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                    val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                }

                override fun onADReceive() {
                    logd("${AdNameType.GDT.type}: onADReceiv")
                    adListener.onAdPrepared(AdNameType.GDT.type)
                }

                override fun onADClicked() {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
                    adListener.onAdClick(AdNameType.GDT.type)
                }

            })
        bannerView?.loadAD()
        bannerView?.setRefresh(30)
        adContainer.visibility = View.VISIBLE
        if (adContainer.childCount > 0) {
            adContainer.removeAllViews()
        }
        adContainer.addView(bannerView)
    }

    interface AdListenerList {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }

}