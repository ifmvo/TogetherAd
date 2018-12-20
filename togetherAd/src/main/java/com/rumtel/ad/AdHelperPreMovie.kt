package com.rumtel.ad

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.rumtel.ad.view.AdViewPreMovieBaidu
import com.rumtel.ad.view.AdViewPreMovieBase
import com.rumtel.ad.view.AdViewPreMovieGDT_old
import java.lang.ref.WeakReference
import java.util.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/8/17.
 */
object AdHelperPreMovie {

    const val tag = "AdHelperPreMovie"
    private var weak: WeakReference<AdViewPreMovieBase>? = null
    private var mAdListener: AdListenerPreMovie? = null
    private var mChannel: String = ""

    /**
     * Activity / Fragment 里面的 onDestroy（） 调用
     */
    fun destroy() {
        cancel()
    }

    fun showAdPreMovie(activity: Activity, configPreMovie: String, adsParentLayout: ViewGroup?, adListener: AdListenerPreMovie?) {
        cancel()
        this.mAdListener = adListener
        if (mAdListener == null) {
            return
        }

        if (adsParentLayout == null) {
            adListener!!.onAdFailed("没有父容器")
            return
        }

        startTimerTask(activity, adsParentLayout, adListener!!)

        adsParentLayout.removeAllViews()
        adsParentLayout.visibility = View.VISIBLE

        val randomAdName = AdRandomUtil.getRandomAdName(configPreMovie)
        when (randomAdName) {
            AdNameType.BAIDU -> {
                showAdPreMovieBaiduMob(activity)
            }
            AdNameType.GDT -> {
                showAdPreMovieGDT(activity)
            }
            else -> {
                adListener.onAdFailed("AdNameType.NO")
                adsParentLayout.visibility = View.GONE
                return
            }
        }

        adListener.onStartRequest(mChannel)

        val adView = weak?.get()
        adView?.setAdViewPreMovieListener(object : AdViewPreMovieBase.AdViewPreMovieListener {
            override fun onAdClick() {
//                Log.e(tag, "前贴广告 $mChannel onAdClick")
                mAdListener!!.onAdClick(mChannel)
                adsParentLayout.visibility = View.GONE

                cancelTimerTask()
            }

            override fun onAdFailed(failedMsg: String) {
//                Log.e(tag, "前贴广告 $mChannel 失败了:$failedMsg")
                var newConfigPreMovie = ""
                when (mChannel) {
                    AdConfig.BAIDU_AD_NAME -> {
                        newConfigPreMovie = configPreMovie.replace("baidu", AdConfig.MASK_NAME)
                    }
                    AdConfig.GDT_AD_NAME -> {
                        newConfigPreMovie = configPreMovie.replace("gdt", AdConfig.MASK_NAME)
                    }
                }
                showAdPreMovie(activity, newConfigPreMovie, adsParentLayout, adListener)

                cancelTimerTask()
            }

            override fun onAdDismissed() {
//                Log.e(tag, "前贴广告 $mChannel onAdDismissed")
                mAdListener!!.onAdDismissed()
                adsParentLayout.visibility = View.GONE

                cancelTimerTask()
            }

            override fun onAdPrepared() {
//                Log.e(tag, "前贴广告 $mChannel onAdPrepared")
                mAdListener!!.onAdPrepared(mChannel)
                adsParentLayout.visibility = View.VISIBLE

                cancelTimerTask()
            }
        })

        if (adView != null) {
            adsParentLayout.addView(adView)
        }
    }


    /**
     * 百度 Mob
     */
    private fun showAdPreMovieBaiduMob(activity: Activity) {
        mChannel = AdConfig.BAIDU_AD_NAME
        weak = WeakReference(AdViewPreMovieBaidu(activity))
    }

    /**
     * 腾讯广点通
     */
    private fun showAdPreMovieGDT(activity: Activity) {
        mChannel = AdConfig.GDT_AD_NAME
        weak = WeakReference(AdViewPreMovieGDT_old(activity))
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
        if (timer != null) {
            timer!!.cancel()
        }
        if (overTimerTask != null) {
            overTimerTask!!.cancel()
        }
    }

    /**
     * 开始计时任务
     */
    private fun startTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdHelperPreMovie.AdListenerPreMovie) {
        cancelTimerTask()
        timer = Timer()
        overTimerTask = OverTimerTask(activity, adsParentLayout, adListener)
        timer!!.schedule(overTimerTask!!, 4000)
    }

    /**
     * 请求超时处理的任务
     */
    internal class OverTimerTask(activity: Activity, adsParentLayout: ViewGroup, adListener: AdListenerPreMovie) : TimerTask() {

        private val weakReference: WeakReference<AdListenerPreMovie>?
        private val weakRefContext: WeakReference<Activity>?
        private val weakRefView: WeakReference<ViewGroup>?

        init {
            weakReference = WeakReference(adListener)
            weakRefContext = WeakReference(activity)
            weakRefView = WeakReference(adsParentLayout)
        }

        override fun run() {
            if (weakReference?.get() != null && weakRefContext?.get() != null && weakRefView?.get() != null) {
                weakRefContext.get()!!.runOnUiThread {
                    weak?.get()?.cancel()
                    weak?.get()?.stop()
                    weak = null
                    weakReference.get()!!.onAdFailed("超时了")
                    weakRefView.get()!!.visibility = View.GONE
                }
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

