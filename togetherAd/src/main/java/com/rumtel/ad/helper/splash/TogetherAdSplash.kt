package com.rumtel.ad.helper.splash

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.CountDownTimer
import android.support.annotation.NonNull
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.iflytek.voiceads.AdKeys
import com.iflytek.voiceads.IFLYNativeAd
import com.iflytek.voiceads.IFLYNativeListener
import com.iflytek.voiceads.NativeADDataRef
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.LoadListener
import com.ifmvo.imageloader.progress.LoaderOptions
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
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
 * (●ﾟωﾟ●) 开屏的广告
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
object TogetherAdSplash : AdBase {

    private var nativeAd: IFLYNativeAd? = null
    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null

    @Volatile
    private var stop = false

    /**
     * 显示开屏广告
     *
     * @param splashConfigStr "baidu:2,gdt:8"
     * @param adsParentLayout 容器
     * @param adListener      监听
     */
    fun showAdFull(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        stop = false
        startTimerTask(activity, adListener)

        val randomAdName = AdRandomUtil.getRandomAdName(splashConfigStr)
        when (randomAdName) {
            AdNameType.BAIDU -> showAdFullBaiduMob(
                activity,
                splashConfigStr,
                adConstStr,
                adsParentLayout,
                adListener
            )
            AdNameType.GDT -> showAdFullGDT(
                activity,
                splashConfigStr,
                adConstStr,
                adsParentLayout,
                adListener
            )
            AdNameType.XUNFEI -> showAdFullXunFei(
                activity,
                splashConfigStr,
                adConstStr,
                adsParentLayout,
                adListener
            )
            else -> {
                if (!stop) {
                    cancelTimerTask()
                    adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                    loge(activity.getString(R.string.all_ad_error))
                }
            }
        }
    }

    /**
     * 腾讯广点通
     */
    private fun showAdFullGDT(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        adListener.onStartRequest(AdNameType.GDT.type)

        SplashAD(
            activity,
            adsParentLayout,
            TogetherAd.appIdGDT,
            TogetherAd.idMapGDT[adConstStr],
            object : SplashADListener {
                override fun onADDismissed() {
                    adListener.onAdDismissed()
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.dismiss)}")
                }

                override fun onNoAD(adError: AdError) {
                    if (!stop) {
                        cancelTimerTask()
                        val newConfigPreMovie = splashConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                        showAdFull(
                            activity,
                            newConfigPreMovie,
                            adConstStr,
                            adsParentLayout,
                            adListener
                        )
                        loge("${AdNameType.GDT.type}: ${adError.errorMsg}")
                    }
                }

                override fun onADPresent() {
                    if (!stop) {
                        cancelTimerTask()
                        adListener.onAdPrepared(AdNameType.GDT.type)
                        logd("${AdNameType.GDT.type}: ${activity.getString(R.string.prepared)}")
                    }
                }

                override fun onADClicked() {
                    adListener.onAdClick(AdNameType.GDT.type)
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
                }

                override fun onADTick(l: Long) {
                    logd("${AdNameType.GDT.type}: 倒计时: $l")
                }

                override fun onADExposure() {
                    logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
                }
            })
    }

    /**
     * 百度Mob
     */
    private fun showAdFullBaiduMob(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {
        adListener.onStartRequest(AdNameType.BAIDU.type)

        SplashAd(activity, adsParentLayout, object : SplashAdListener {
            override fun onAdPresent() {
                if (!stop) {
                    cancelTimerTask()
                    adListener.onAdPrepared(AdNameType.BAIDU.type)
                    logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
                }
            }

            override fun onAdDismissed() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                adListener.onAdDismissed()
            }

            override fun onAdFailed(s: String) {
                loge("${AdNameType.BAIDU.type}: $s")
                val newConfigPreMovie = splashConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdFull(
                    activity,
                    newConfigPreMovie,
                    adConstStr,
                    adsParentLayout,
                    adListener
                )
            }

            override fun onAdClick() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                adListener.onAdClick(AdNameType.BAIDU.type)
            }

        }, TogetherAd.idMapBaidu[adConstStr], true)
    }

    /**
     * 科大讯飞
     */
    private fun showAdFullXunFei(
        @NonNull activity: Activity,
        splashConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerSplashFull
    ) {

        adListener.onStartRequest(AdNameType.XUNFEI.type)
        nativeAd = IFLYNativeAd(activity, TogetherAd.idMapXunFei[adConstStr], object : IFLYNativeListener {
            override fun onADLoaded(list: List<NativeADDataRef>?) {

                if (list == null || list.isEmpty()) {
                    val newConfigPreMovie = splashConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                    showAdFull(
                        activity,
                        newConfigPreMovie,
                        adConstStr,
                        adsParentLayout,
                        adListener
                    )
                    return
                }

                val adItem = list[0]
                val adPic = ImageView(activity)
                val params = RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                adPic.scaleType = ImageView.ScaleType.CENTER_CROP
                adsParentLayout.addView(adPic, params)
                ILFactory.getLoader().load(activity, adPic, adItem.image, LoaderOptions(), object : LoadListener() {
                    override fun onLoadCompleted(drawable: Drawable): Boolean {

                        if (!stop) {
                            cancelTimerTask()
                            adListener.onAdPrepared(AdNameType.XUNFEI.type)
                            logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.prepared)}")
                        }

                        if (adItem.onExposured(adPic)) {
                            logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.exposure)}")
                        }

                        object : CountDownTimer(5000, 1000) {
                            override fun onTick(millisUntilFinished: Long) {
                                logd("${AdNameType.XUNFEI.type}: 倒计时: $millisUntilFinished")
                            }

                            override fun onFinish() {
                                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.dismiss)}")
                                nativeAd = null
                                adListener.onAdDismissed()
                            }
                        }.start()
                        return false
                    }
                })
                adsParentLayout.setOnClickListener { view ->
                    adItem.onClicked(view)
                    adListener.onAdClick(AdNameType.XUNFEI.type)
                }
                adsParentLayout.setOnTouchListener { v, event ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            nativeAd?.setParameter(AdKeys.CLICK_POS_DX, event.x.toString() + "")
                            nativeAd?.setParameter(AdKeys.CLICK_POS_DY, event.y.toString() + "")
                        }
                        MotionEvent.ACTION_UP -> {
                            nativeAd?.setParameter(AdKeys.CLICK_POS_UX, event.x.toString() + "")
                            nativeAd?.setParameter(AdKeys.CLICK_POS_UY, event.y.toString() + "")
                        }
                        else -> {
                        }
                    }
                    false
                }
            }

            override fun onAdFailed(adError: com.iflytek.voiceads.AdError) {
                if (!stop) {
                    cancelTimerTask()
                    val newConfigPreMovie = splashConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                    showAdFull(
                        activity,
                        newConfigPreMovie,
                        adConstStr,
                        adsParentLayout,
                        adListener
                    )
                    loge("${AdNameType.XUNFEI.type}: ${adError.errorCode}, ${adError.errorDescription}")
                }
            }

            override fun onConfirm() {

            }

            override fun onCancel() {

            }
        })
        val count = 1 // 一次拉取的广告条数:范围 1-30(目前仅支持每次请求一条)
        nativeAd?.loadAd(count)
    }

    /**
     * 监听器
     */
    interface AdListenerSplashFull {
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
    private fun startTimerTask(activity: Activity, listener: AdListenerSplashFull) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask =
                OverTimerTask(activity, listener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 超时任务
     */
    private class OverTimerTask(activity: Activity, listener: AdListenerSplashFull) : TimerTask() {

        private val weakReference: WeakReference<AdListenerSplashFull>?
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
                nativeAd = null
                timer = null
                overTimerTask = null
            }
        }
    }

}