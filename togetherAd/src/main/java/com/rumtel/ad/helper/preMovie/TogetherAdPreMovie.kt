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
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieGDT
import com.rumtel.ad.helper.preMovie.view.AdViewPreMovieIXunFei
import com.rumtel.ad.other.AdNameType
import com.rumtel.ad.other.AdRandomUtil
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●) 前贴的广告
 * 
 * Created by Matthew_Chen on 2018/8/17.
 */
object TogetherAdPreMovie : AdBase {

    private var weak: WeakReference<AdViewPreMovieBase>? = null
    private var mAdListener: AdListenerPreMovie? = null
    private var mChannel: String = ""

    /**
     * Activity / Fragment 里面的 onDestroy（） 调用
     */
    fun destroy() {
        cancel()
    }

    fun showAdPreMovie(
        @NonNull activity: Activity,
        configPreMovie: String?,
        @NonNull adConstStr: String,
        @NonNull adsParentLayout: ViewGroup,
        @NonNull adListener: AdListenerPreMovie
    ) {
        startTimerTask(activity, adsParentLayout, adListener)
        cancel()
        mAdListener = adListener
        if (mAdListener == null) {
            return
        }

        adsParentLayout.visibility = View.VISIBLE
        if (adsParentLayout.childCount > 0) {
            adsParentLayout.removeAllViews()
        }

        val randomAdName = AdRandomUtil.getRandomAdName(configPreMovie)
        when (randomAdName) {
            AdNameType.BAIDU -> {
                showAdPreMovieBaiduMob(activity)
            }
            AdNameType.GDT -> {
                showAdPreMovieGDT(activity)
            }
            AdNameType.XUNFEI -> {
                showAdPreMovieIFly(activity)
            }
            else -> {
                cancelTimerTask()
                loge(activity.getString(R.string.all_ad_error))
                adListener.onAdFailed(activity.getString(R.string.all_ad_error))
                adsParentLayout.visibility = View.GONE
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
                mAdListener?.onAdClick(mChannel)
                adsParentLayout.visibility = View.GONE
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
                    AdNameType.XUNFEI.type -> {
                        newConfigPreMovie = configPreMovie?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                    }
                    else -> {
                        mAdListener?.onAdFailed(failedMsg)
                    }
                }

                showAdPreMovie(
                    activity,
                    newConfigPreMovie,
                    adConstStr,
                    adsParentLayout,
                    adListener
                )
            }

            override fun onAdDismissed() {
                mAdListener?.onAdDismissed()
                adsParentLayout.visibility = View.GONE
                logd("$mChannel: ${activity.getString(R.string.dismiss)}")
            }

            override fun onAdPrepared() {
                mAdListener?.onAdPrepared(mChannel)
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
            AdNameType.XUNFEI.type -> {
                TogetherAd.idMapXunFei[adConstStr]
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
    private fun showAdPreMovieBaiduMob(activity: Activity) {
        mChannel = AdNameType.BAIDU.type
        weak = WeakReference(AdViewPreMovieBaidu(activity))
    }

    /**
     * 腾讯广点通
     */
    private fun showAdPreMovieGDT(activity: Activity) {
        mChannel = AdNameType.GDT.type
        weak = WeakReference(AdViewPreMovieGDT(activity))
    }

    /**
     * 科大讯飞
     */
    private fun showAdPreMovieIFly(activity: Activity) {
        mChannel = AdNameType.XUNFEI.type
        weak = WeakReference(AdViewPreMovieIXunFei(activity))
    }

    private fun cancel() {
        val lastAdView = weak?.get()
        lastAdView?.cancel()
        lastAdView?.stop()
        weak = null
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
        overTimerTask =
                OverTimerTask(activity, adsParentLayout, adListener)
        timer?.schedule(overTimerTask, TogetherAd.timeOutMillis)
    }

    /**
     * 请求超时处理的任务
     */
    private class OverTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerPreMovie) :
        TimerTask() {

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
                weak?.get()?.cancel()
                weak?.get()?.stop()
                weak = null
                weakReference?.get()?.onAdFailed(weakRefContext.get()?.getString(R.string.timeout))
                loge(weakRefContext.get()?.getString(R.string.timeout))
                weakRefView?.get()?.visibility = View.GONE
            }
        }
    }

    interface AdListenerPreMovie {

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)

        fun onStartRequest(channel: String)
    }
}

