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
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 信息流的广告
 * 
 * Created by Matthew_Chen on 2018/12/25.
 */
object TogetherAdBanner : AdBase {

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null
    @Volatile
    private var stop = false

    fun requestBanner(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adContainer: FrameLayout,
        @NonNull adListener: AdListenerList
    ) {
        stop = false
        startTimerTask(activity, adListener)

        val randomAdName = AdRandomUtil.getRandomAdName(listConfigStr)
        when (randomAdName) {
            AdNameType.BAIDU -> requestBannerBaidu(activity, listConfigStr, adConstStr, adContainer, adListener)
            AdNameType.GDT -> requestBannerGDT(activity, listConfigStr, adConstStr, adContainer, adListener)
            AdNameType.CSJ -> requestBannerCsj(activity, listConfigStr, adConstStr, adContainer, adListener)
            else -> {
                if (stop) {
                    return
                }
                cancelTimerTask()

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
                    if (stop) {
                        return
                    }

                    if (list.isNullOrEmpty()) {
                        loge("${AdNameType.BAIDU.type}: 返回的广告是空的")
                        val newListConfig = listConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                        requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                        return
                    }

                    cancelTimerTask()

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
                    val w = dm.widthPixels / 3
                    val h = w * 9 / 16
                    val layoutParams = adContainer.layoutParams
                    layoutParams.height = h

                    val ivLayoutParams = ivImage.layoutParams
                    ivLayoutParams.width = w

                    ILFactory.getLoader()
                        .load(activity, ivImage, adItem.imageUrl, LoaderOptions(), object : LoadListener() {
                            override fun onLoadCompleted(p0: Drawable?): Boolean {
                                return true
                            }
                        })
                    adContainer.addView(superView)
                    adItem.recordImpression(superView)
                    superView.setOnClickListener {
                        adItem.handleClick(it)
                    }

                    ivClose.setOnClickListener {
                        logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                        adContainer.removeAllViews()
                        adContainer.visibility = View.GONE
                        adListener.onAdDismissed()
                    }

                }

                override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

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
        val w = dm.widthPixels / 3
        val h = w * 9 / 16

        val adSlot = AdSlot.Builder()
            .setCodeId(TogetherAd.idMapCsj[adConstStr])
            .setSupportDeepLink(true)
            .setImageAcceptedSize(dm.widthPixels, (dm.widthPixels * 9 / 16))
            .setAdCount(1)
            .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadFeedAd(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(adList: MutableList<TTFeedAd>?) {
                if (stop) {
                    return
                }
                if (adList.isNullOrEmpty()) {
                    loge("${AdNameType.CSJ.type}: 返回的广告是空的")
                    val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                    return
                }

                cancelTimerTask()

                logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.prepared)}")
                adListener.onAdPrepared(AdNameType.CSJ.type)

                //获取一个广告
                val adItem = adList[0]

                val superView = View.inflate(activity, R.layout.layout_banner_view, null)
                val ivImage = superView.findViewById<ImageView>(R.id.iv_img)
                val ivClose = superView.findViewById<ImageView>(R.id.iv_close)
                val tvTitle = superView.findViewById<TextView>(R.id.tv_title)
                val tvDesc = superView.findViewById<TextView>(R.id.tv_desc)

//                ivClose.setOnClickListener {
//                    logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
//                    adContainer.removeAllViews()
//                    adContainer.visibility = View.GONE
//                    adListener.onAdDismissed()
//                }

//                //广告标示
//                val logoViewParams = RelativeLayout.LayoutParams(60, 60)
//
//                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
//                logoViewParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
//
//                val logoView = ImageView(activity)
//                logoView.layoutParams = logoViewParams
//                logoView.setImageBitmap(adItem.adLogo)


                //加载ad 图片资源
                val imageList = adItem.imageList
                if (imageList.isNullOrEmpty() || imageList[0] == null) {
                    loge("${AdNameType.CSJ.type}: 广告里面的图片是null")
                    val newConfigStr = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    requestBanner(activity, newConfigStr, adConstStr, adContainer, adListener)
                    return
                }

                adContainer.visibility = View.VISIBLE
                if (adContainer.childCount > 0) {
                    adContainer.removeAllViews()
                }

                val layoutParams = adContainer.layoutParams
                layoutParams.height = h

                val ivLayoutParams = ivImage.layoutParams
                ivLayoutParams.width = w

                tvTitle.text = adItem.title
                tvDesc.text = adItem.description

                val ttImage = imageList[0]
                ILFactory.getLoader()
                    .load(activity, ivImage, ttImage.imageUrl, LoaderOptions(), object : LoadListener() {
                        override fun onLoadCompleted(p0: Drawable?): Boolean {
                            return true
                        }
                    })
                adContainer.addView(superView)

//                //不喜欢的标识, 绑定网盟dislike逻辑，有助于精准投放
//                val dislikeDialog = adItem.getDislikeDialog(activity)
//                dislikeDialog?.setDislikeInteractionCallback(object : TTAdDislike.DislikeInteractionCallback {
//                    override fun onSelected(position: Int, value: String?) {
//                        logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
//                        adContainer.removeAllViews()
//                        adContainer.visibility = View.GONE
//                        adListener.onAdDismissed()
//                    }
//
//                    override fun onCancel() {
//                    }
//                })
                ivClose.setOnClickListener {
                    //                    dislikeDialog?.showDislikeDialog()
                    logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                    adContainer.removeAllViews()
                    adContainer.visibility = View.GONE
                    adListener.onAdDismissed()
                }

                //绑定广告view事件交互
                val clickViewList = mutableListOf<View>()
                clickViewList.add(superView)
                adItem.registerViewForInteraction(adContainer, clickViewList, clickViewList, null,
                    object : TTNativeAd.AdInteractionListener {
                        override fun onAdClicked(p0: View?, p1: TTNativeAd?) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                            adContainer.removeAllViews()
                            adContainer.visibility = View.GONE
                            adListener.onAdDismissed()
                        }

                        override fun onAdShow(p0: TTNativeAd?) {
                        }

                        override fun onAdCreativeClick(p0: View?, p1: TTNativeAd?) {
                            logd("${AdNameType.CSJ.type}: ${activity.getString(R.string.dismiss)}")
                            adContainer.removeAllViews()
                            adContainer.visibility = View.GONE
                            adListener.onAdDismissed()
                        }
                    })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                if (stop) {
                    return
                }
                cancelTimerTask()

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
                    loge("${AdNameType.GDT.type}: ${activity.getString(R.string.dismiss)}")
                    adListener.onAdDismissed()
                    bannerView?.destroy()
                    bannerView = null
                }

                override fun onADLeftApplication() {
                }

                override fun onADOpenOverlay() {
                }

                override fun onNoAD(adError: AdError?) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()
                    loge("${AdNameType.GDT.type}: ${adError?.errorCode}, ${adError?.errorMsg}")
                    val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    requestBanner(activity, newListConfig, adConstStr, adContainer, adListener)
                }

                override fun onADReceive() {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()
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

    /**
     * 取消超时任务
     */
    private fun cancelTimerTask() {
        stop = false
        timer?.cancel()
        overTimerTask?.cancel()
    }

    /**
     * 开始超时任务
     */
    private fun startTimerTask(activity: Activity, listener: AdListenerList) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask =
            OverTimerTask(activity, listener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 超时任务
     */
    private class OverTimerTask(activity: Activity, listener: AdListenerList) : TimerTask() {

        private val weakReference: WeakReference<AdListenerList>?
        private val weakRefContext: WeakReference<Activity>?

        init {
            weakReference = WeakReference(listener)
            weakRefContext = WeakReference(activity)
        }

        override fun run() {
            stop = true
            weakRefContext?.get()?.runOnUiThread {
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                timer = null
                overTimerTask = null
            }
        }
    }
}