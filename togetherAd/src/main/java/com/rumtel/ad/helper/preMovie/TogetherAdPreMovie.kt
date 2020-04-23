package com.rumtel.ad.helper.preMovie

import android.app.Activity
import android.support.annotation.NonNull
import android.view.View
import android.view.ViewGroup
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.helper.AdBase
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieBaidu
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieBase
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieCsj
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieGDT
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 前贴的广告 （ 视频播放之前展示、可配置是否倒计时 ）
 * 
 * Created by Matthew_Chen on 2018/8/17.
 */
object TogetherAdPreMovie : AdBase() {

    private var weak: WeakReference<AdViewPreMovieBase>? = null
    private var mChannel: String = ""

    fun showAdPreMovie(@NonNull activity: Activity, configPreMovie: String?, @NonNull adConstStr: String, @NonNull adsParentLayout: ViewGroup, @NonNull adListener: AdListenerPreMovie, @NonNull needTimer: Boolean = true) {
        startTimerTask(activity, adsParentLayout, adListener)
        //如果存在，首先销毁上一个广告
        destroy()

        adsParentLayout.visibility = View.VISIBLE
        if (adsParentLayout.childCount > 0) {
            adsParentLayout.removeAllViews()
        }

        when (AdRandomUtil.getRandomAdName(configPreMovie)) {
            AdNameType.BAIDU -> {
                showAdPreMovieBaiduMob(activity, needTimer)
            }
            AdNameType.GDT -> {
                showAdPreMovieGDT(activity, needTimer)
            }
            AdNameType.CSJ -> {
                showAdPreMovieCsj(activity, needTimer)
            }
            else -> {
                cancelTimerTask()
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                destroy()
                adsParentLayout.removeAllViews()
                return
            }
        }

        adListener.onStartRequest(mChannel)

        val adView = weak?.get()

        if (adView != null) {
            adsParentLayout.addView(adView)
        }

        adView?.setAdViewPreMovieListener(object : AdViewPreMovieBase.AdViewPreMovieListener {
            override fun onExposured() {
                logd("$mChannel: ${activity.getString(R.string.exposure)}")
            }

            override fun onAdClick() {
                adListener.onAdClick(mChannel)
                logd("$mChannel: ${activity.getString(R.string.clicked)}")
            }

            override fun onAdFailed(failedMsg: String) {
                loge("$mChannel: $failedMsg")

                var newConfigPreMovie: String? = null
                when (mChannel) {
                    AdNameType.BAIDU.type -> {
                        newConfigPreMovie = configPreMovie?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                    }
                    AdNameType.GDT.type -> {
                        newConfigPreMovie = configPreMovie?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                    }
                    AdNameType.CSJ.type -> {
                        newConfigPreMovie = configPreMovie?.replace(AdNameType.CSJ.type, AdNameType.NO.type)
                    }
                    else -> {
                        adListener.onAdFailed(failedMsg)
                    }
                }

                activity.runOnUiThread {
                    showAdPreMovie(activity, newConfigPreMovie, adConstStr, adsParentLayout, adListener, needTimer)
                }
            }

            override fun onAdDismissed() {
                adListener.onAdDismissed()
                /**
                 * 这里不能销毁广告
                 * 如果销毁广告，广告详情页就无法正常观看了
                 * Activity 的 onDestroy 会将其销毁
                 * 并且下次再请求广告也会先销毁上一个广告
                 * 所以不用担心内存泄漏
                 */
//                destroy()
                adsParentLayout.removeAllViews()
                logd("$mChannel: ${activity.getString(R.string.dismiss)}")
            }

            override fun onAdPrepared() {
                adListener.onAdPrepared(mChannel)
                adsParentLayout.visibility = View.VISIBLE
                cancelTimerTask()
                logd("$mChannel: ${activity.getString((R.string.prepared))}")
            }
        })?.start(getLocationIdFromMap(adConstStr))
    }

    private fun getLocationIdFromMap(adConstStr: String): String? {
        return when (mChannel) {
            AdNameType.GDT.type -> {
                TogetherAd.idMapGDT[adConstStr]
            }
            AdNameType.BAIDU.type -> {
                TogetherAd.idMapBaidu[adConstStr]
            }
            AdNameType.CSJ.type -> {
                TogetherAd.idMapCsj[adConstStr]
            }
            else -> {
                loge("发生了不可能的灵异事件")
                ""
            }
        }
    }


    /**
     * 百度 Mob
     */
    private fun showAdPreMovieBaiduMob(activity: Activity, @NonNull needTimer: Boolean) {
        mChannel = AdNameType.BAIDU.type
        weak = WeakReference(AdViewPreMovieBaidu(activity, needTimer))
    }

    /**
     * 腾讯广点通
     */
    private fun showAdPreMovieGDT(activity: Activity, @NonNull needTimer: Boolean) {
        mChannel = AdNameType.GDT.type
        weak = WeakReference(AdViewPreMovieGDT(activity, needTimer))
    }

    /**
     * 穿山甲
     */
    private fun showAdPreMovieCsj(activity: Activity, @NonNull needTimer: Boolean) {
        mChannel = AdNameType.CSJ.type
        weak = WeakReference(AdViewPreMovieCsj(activity, needTimer))
    }

    private var timer: Timer? = null
    private var overTimerTask: OverTimerTask? = null

    /**
     * 取消计时任务
     */
    private fun cancelTimerTask() {
        timer?.cancel()
        overTimerTask?.cancel()
    }

    /**
     * 开始计时任务
     */
    private fun startTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerPreMovie) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(activity, adsParentLayout, adListener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 请求超时处理的任务
     */
    private class OverTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerPreMovie) : TimerTask() {

        private val weakReference: WeakReference<AdListenerPreMovie>?
        private val weakRefContext: WeakReference<Activity>?
        private val weakRefView: WeakReference<ViewGroup>?

        init {
            weakReference = WeakReference(adListener)
            weakRefContext = WeakReference(activity)
            weakRefView = WeakReference(adsParentLayout)
        }

        override fun run() {
            weakRefContext?.get()?.runOnUiThread {
                weak?.get()?.destroy()
                weak = null
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                weakRefView?.get()?.visibility = View.GONE
            }
        }
    }

    /**
     * Activity / Fragment 里面的 onDestroy（） 调用
     */
    fun destroy() {
        val lastAdView = weak?.get()
        lastAdView?.destroy()
        weak = null
    }

    fun resume() {
        val lastAdView = weak?.get()
        lastAdView?.resume()
    }

    fun pause() {
        val lastAdView = weak?.get()
        lastAdView?.pause()
    }

    interface AdListenerPreMovie {

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)

        fun onStartRequest(channel: String)
    }
}

