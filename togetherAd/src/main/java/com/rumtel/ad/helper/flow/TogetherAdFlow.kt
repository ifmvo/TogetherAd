package com.rumtel.ad.helper.flow

import android.app.Activity
import android.support.annotation.NonNull
import android.util.DisplayMetrics
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTFeedAd
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
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 信息流的广告
 * 
 * Created by Matthew_Chen on 2018/12/25.
 */
object TogetherAdFlow : AdBase() {

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null
    @Volatile
    private var stop = false

    fun getAdList(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adListener: AdListenerList
    ) {
        stop = false
        startTimerTask(activity, adListener)

        val randomAdName = AdRandomUtil.getRandomAdName(listConfigStr)
        when (randomAdName) {
            AdNameType.BAIDU -> getAdListBaiduMob(activity, listConfigStr, adConstStr, adListener)
            AdNameType.GDT -> getAdListTecentGDT(activity, listConfigStr, adConstStr, adListener)
            AdNameType.CSJ -> getAdListCsj(activity, listConfigStr, adConstStr, adListener)
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

    private fun getAdListBaiduMob(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.BAIDU.type)
        val baidu = BaiduNative(
            activity,
            TogetherAd.idMapBaidu[adConstStr],
            object : BaiduNative.BaiduNativeNetworkListener {

                override fun onNativeLoad(list: List<NativeResponse>) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    activity.runOnUiThread {
                        adListener.onAdLoaded(AdNameType.BAIDU.type, list)
                    }
                    logd("${AdNameType.BAIDU.type}: list.size: " + list.size)
                }

                override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                    if (stop) {
                        return
                    }
                    cancelTimerTask()

                    val newListConfig = listConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
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

    private fun getAdListCsj(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.CSJ.type)

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)
        val adSlot = AdSlot.Builder()
            .setCodeId(TogetherAd.idMapCsj[adConstStr])
            .setSupportDeepLink(true)
            .setImageAcceptedSize(dm.widthPixels, (dm.widthPixels * 9 / 16))
            .setAdCount(4)
            .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadFeedAd(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(adList: MutableList<TTFeedAd>?) {
                if (stop) {
                    return
                }
                if (adList.isNullOrEmpty()) {
                    loge("${AdNameType.CSJ.type}: 返回的广告是空的")
                    val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
                    return
                }


                cancelTimerTask()

                adListener.onAdLoaded(AdNameType.CSJ.type, adList)
                logd("${AdNameType.CSJ.type}: list.size: " + adList.size)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                loge("${AdNameType.CSJ.type}: errorCode: $errorCode, errorMsg: $errorMsg")
                val newListConfig = listConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                getAdList(activity, newListConfig, adConstStr, adListener)
            }
        })
    }

    private fun getAdListTecentGDT(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.GDT.type)

        val adListenerNative = object : NativeMediaAD.NativeMediaADListener {
            override fun onADLoaded(adList: List<NativeMediaADData>) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                logd("${AdNameType.GDT.type}: list.size: " + adList.size)
                adList.forEach {
                    logd("${AdNameType.GDT.type}, ecpm: ${it.ecpm}, ecpmLevel: ${it.ecpmLevel}")
                }
                activity.runOnUiThread {
                    adListener.onAdLoaded(AdNameType.GDT.type, adList)
                }
            }

            override fun onNoAD(adError: AdError) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                loge("${AdNameType.GDT.type}: ${adError.errorCode}, ${adError.errorMsg}")
                val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                getAdList(activity, newListConfig, adConstStr, adListener)
            }

            override fun onADStatusChanged(ad: NativeMediaADData) {}

            override fun onADError(adData: NativeMediaADData, adError: AdError) {
                if (stop) {
                    return
                }
                cancelTimerTask()

                loge("${AdNameType.GDT.type}: ${adError.errorCode}, ${adError.errorMsg}")
                val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                getAdList(activity, newListConfig, adConstStr, adListener)
            }

            override fun onADVideoLoaded(adData: NativeMediaADData) {
                logd("${AdNameType.GDT.type}: 视频素材加载完成")
            }

            override fun onADExposure(adData: NativeMediaADData) {
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
            }

            override fun onADClicked(adData: NativeMediaADData) {
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
            }
        }

        val mADManager = NativeMediaAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], adListenerNative)
        mADManager.setMaxVideoDuration(60)
        mADManager.loadAD(4)
    }

    interface AdListenerList {

        fun onAdFailed(failedMsg: String?)

        fun onAdLoaded(channel: String, adList: List<*>)

        fun onStartRequest(channel: String)
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