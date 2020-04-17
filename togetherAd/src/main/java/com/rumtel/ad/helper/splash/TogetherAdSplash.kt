package com.rumtel.ad.helper.splash

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import com.baidu.mobads.SplashAd
import com.baidu.mobads.SplashAdListener
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.TogetherAd.mContext
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.util.*

/* 
 * (●ﾟωﾟ●) 开屏的广告 （ 打开应用的时候展示 ）
 * 
 * Created by Matthew_Chen on 2018/12/24.
 */
object TogetherAdSplash : AdBase() {

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
    fun showAdFull(@NonNull activity: Activity, splashConfigStr: String?, @NonNull adConstStr: String, @NonNull adsParentLayout: ViewGroup, skipView: View? = null, timeView: TextView? = null, @NonNull adListener: AdListenerSplashFull) {
        stop = false
        startTimerTask(adListener)

        when (AdRandomUtil.getRandomAdName(splashConfigStr)) {
            AdNameType.BAIDU -> {
                showAdFullBaiduMob(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener)
            }
            AdNameType.GDT -> {
                showAdFullGDT(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener)
            }
            AdNameType.CSJ -> {
                showAdFullCsj(activity, splashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener)
            }
            else -> {
                if (stop) {
                    return
                }
                cancelTimerTask()

                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                loge(activity.getString(R.string.all_ad_error))
            }
        }
    }

    /**
     * 腾讯广点通
     */
    private fun showAdFullGDT(@NonNull activity: Activity, splashConfigStr: String?, @NonNull adConstStr: String, @NonNull adsParentLayout: ViewGroup, skipView: View?, timeView: TextView?, @NonNull adListener: AdListenerSplashFull) {

    }

    /**
     * 百度Mob
     */
    private fun showAdFullBaiduMob(@NonNull activity: Activity, splashConfigStr: String?, @NonNull adConstStr: String, @NonNull adsParentLayout: ViewGroup, skipView: View?, timeView: TextView?, @NonNull adListener: AdListenerSplashFull) {
        adListener.onStartRequest(AdNameType.BAIDU.type)

        SplashAd(activity, adsParentLayout, object : SplashAdListener {
            override fun onAdPresent() {
                if (stop) {
                    return
                }
                cancelTimerTask()

                adListener.onAdPrepared(AdNameType.BAIDU.type)
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
            }

            override fun onAdDismissed() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
                adListener.onAdDismissed()
            }

            override fun onAdFailed(s: String) {
                if (stop) {
                    return
                }
                cancelTimerTask()
                loge("${AdNameType.BAIDU.type}: $s")
                val newConfigPreMovie = splashConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdFull(activity, newConfigPreMovie, adConstStr, adsParentLayout, skipView, timeView, adListener)
            }

            override fun onAdClick() {
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
                adListener.onAdClick(AdNameType.BAIDU.type)
            }

        }, TogetherAd.idMapBaidu[adConstStr], true)
    }

    /**
     * 穿山甲
     */
    private fun showAdFullCsj(@NonNull activity: Activity, splashConfigStr: String?, @NonNull adConstStr: String, @NonNull adsParentLayout: ViewGroup, skipView: View?, timeView: TextView?, @NonNull adListener: AdListenerSplashFull) {
        try {

        } catch (e: Exception) {
            if (stop) {
                return
            }
            cancelTimerTask()

            loge("${AdNameType.CSJ.type}: 线程：${Thread.currentThread().name}, 崩溃异常: $e")
            val newSplashConfigStr = splashConfigStr?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
            showAdFull(activity, newSplashConfigStr, adConstStr, adsParentLayout, skipView, timeView, adListener)
        }
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
    private fun startTimerTask(listener: AdListenerSplashFull) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(listener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 超时任务
     */
    private class OverTimerTask(listener: AdListenerSplashFull) : TimerTask() {

        private var weakReference: AdListenerSplashFull?
//        private var weakRefContext: Activity?

        init {
            weakReference = listener
//            weakRefContext = mContext
        }

        override fun run() {
            stop = true
//            weakRefContext?.runOnUiThread {
            weakReference?.onAdFailed(mContext.getString(R.string.timeout))
            loge(mContext.getString(R.string.timeout) + weakReference)
            timer = null
            overTimerTask = null
//            }
            weakReference = null
//            weakRefContext = null
        }
    }
}