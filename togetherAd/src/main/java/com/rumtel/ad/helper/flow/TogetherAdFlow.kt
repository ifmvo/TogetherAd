package com.rumtel.ad.helper.flow

import android.app.Activity
import android.support.annotation.NonNull
import com.baidu.mobad.feeds.BaiduNative
import com.baidu.mobad.feeds.NativeErrorCode
import com.baidu.mobad.feeds.NativeResponse
import com.baidu.mobad.feeds.RequestParameters
import com.iflytek.voiceads.IFLYNativeAd
import com.iflytek.voiceads.IFLYNativeListener
import com.iflytek.voiceads.NativeADDataRef
import com.qq.e.ads.nativ.NativeMediaAD
import com.qq.e.ads.nativ.NativeMediaADData
import com.qq.e.comm.util.AdError
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 信息流的广告
 * 
 * Created by Matthew_Chen on 2018/12/25.
 */
object TogetherAdFlow : AdBase {

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
            AdNameType.BAIDU -> getAdListBaiduMob(
                activity,
                listConfigStr,
                adConstStr,
                adListener
            )
            AdNameType.GDT -> getAdListTecentGDT(
                activity,
                listConfigStr,
                adConstStr,
                adListener
            )
            AdNameType.XUNFEI -> getAdListIFly(
                activity,
                listConfigStr,
                adConstStr,
                adListener
            )
            else -> {
                if (!stop) {
                    cancelTimerTask()
                    adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                }
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
                    if (!stop) {
                        cancelTimerTask()
                        adListener.onAdLoaded(AdNameType.BAIDU.type, list)
                        logd("${AdNameType.BAIDU.type}: list.size: " + list.size)
                    }
                }

                override fun onNativeFail(nativeErrorCode: NativeErrorCode) {
                    if (!stop) {
                        cancelTimerTask()
                        val newListConfig = listConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                        getAdList(
                            activity,
                            newListConfig,
                            adConstStr,
                            adListener
                        )
                        loge("${AdNameType.BAIDU.type}: nativeErrorCode: $nativeErrorCode")
                    }
                }
            })
        /*
         * Step 2. 创建requestParameters对象，并将其传给baidu.makeRequest来请求广告
         */
        // 用户点击下载类广告时，是否弹出提示框让用户选择下载与否
        val requestParameters = RequestParameters.Builder()
            //                        .downloadAppConfirmPolicy(RequestParameters.DOWNLOAD_APP_CONFIRM_NEVER)
            .build()

        baidu.makeRequest(requestParameters)
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
                if (!stop) {
                    cancelTimerTask()
                    adListener.onAdLoaded(AdNameType.GDT.type, adList)
                    logd("${AdNameType.GDT.type}: list.size: " + adList.size)
                }
            }

            override fun onNoAD(adError: AdError) {
                if (!stop) {
                    cancelTimerTask()
                    val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
                    loge("${AdNameType.GDT.type}: ${adError.errorCode}, ${adError.errorMsg}")
                }
            }

            override fun onADStatusChanged(ad: NativeMediaADData) {}

            override fun onADError(adData: NativeMediaADData, adError: AdError) {
                if (!stop) {
                    cancelTimerTask()
                    val newListConfig = listConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
                    loge("${AdNameType.GDT.type}: ${adError.errorCode}, ${adError.errorMsg}")
                }
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

        val mADManager =
            NativeMediaAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr], adListenerNative)
        mADManager.loadAD(4)
    }

    private var mListener: IFLYNativeListener? = null

    private fun getAdListIFly(
        @NonNull activity: Activity,
        listConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adListener: AdListenerList
    ) {
        adListener.onStartRequest(AdNameType.XUNFEI.type)
        mListener = object : IFLYNativeListener {
            override fun onConfirm() {

            }

            override fun onCancel() {

            }

            override fun onADLoaded(list: List<NativeADDataRef>) {
                if (!stop) {
                    cancelTimerTask()
                    adListener.onAdLoaded(AdNameType.XUNFEI.type, list)
                    logd("${AdNameType.XUNFEI.type}: list.size: " + list.size)
                }
            }

            override fun onAdFailed(adError: com.iflytek.voiceads.AdError) {
                if (!stop) {
                    cancelTimerTask()
                    val newListConfig = listConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                    getAdList(activity, newListConfig, adConstStr, adListener)
                    loge("${AdNameType.XUNFEI.type}: ${adError.errorCode}, ${adError.errorDescription}")
                }
            }
        }

        val nativeAd = IFLYNativeAd(activity, TogetherAd.idMapXunFei[adConstStr],
            mListener
        )
        val count = 1 // 一次拉取的广告条数:范围 1-30(目前仅支持每次请求一条)
        nativeAd.loadAd(count)

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
            mListener = null
            weakRefContext?.get()?.runOnUiThread {
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                timer = null
                overTimerTask = null
            }
        }
    }
}