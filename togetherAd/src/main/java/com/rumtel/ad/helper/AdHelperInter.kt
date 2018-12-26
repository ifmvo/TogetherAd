package com.rumtel.ad.helper

import android.app.Activity
import android.support.annotation.NonNull
import android.text.TextUtils
import android.util.DisplayMetrics
import android.view.View
import android.widget.RelativeLayout
import com.baidu.mobads.AdSize
import com.baidu.mobads.InterstitialAd
import com.baidu.mobads.InterstitialAdListener
import com.iflytek.voiceads.IFLYAdListener
import com.iflytek.voiceads.IFLYAdSize
import com.iflytek.voiceads.IFLYInterstitialAd
import com.qq.e.ads.interstitial.InterstitialAD
import com.qq.e.ads.interstitial.InterstitialADListener
import com.qq.e.comm.util.AdError
import com.rumtel.ad.AdNameType
import com.rumtel.ad.AdRandomUtil
import com.rumtel.ad.R
import com.rumtel.ad.TogetherAd
import com.rumtel.ad.other.logd
import com.rumtel.ad.other.loge

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
object AdHelperInter : AdHelperBase {

    fun showAdInter(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {
        var newConfigStr = interConfigStr

        //目前这个版本先这样写，横屏下广点通大概率展示不出来的问题
        if (isLandscape && !TextUtils.isEmpty(interConfigStr)) {
            newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
        }

        val randomAdName = AdRandomUtil.getRandomAdName(newConfigStr)
        when (randomAdName) {
            AdNameType.BAIDU -> showAdInterBaiduMob(
                activity,
                newConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            AdNameType.GDT -> showAdInterTecentGDT(
                activity,
                newConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            AdNameType.XUNFEI -> showAdInterIFLY(
                activity,
                newConfigStr,
                adConstStr,
                isLandscape,
                adIntersContainer,
                adListener
            )
            else -> adListener.onAdFailed(activity.getString(R.string.all_ad_error))
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
    private fun showAdInterTecentGDT(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {

        adListener.onStartRequest(AdNameType.GDT.type)
        val iad = InterstitialAD(activity, TogetherAd.appIdGDT, TogetherAd.idMapGDT[adConstStr])
        iad.setADListener(object : InterstitialADListener {
            override fun onADReceive() {
                iad.show()
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.show)}")
            }

            override fun onNoAD(error: AdError) {
                val newConfigStr = interConfigStr?.replace(AdNameType.GDT.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                loge("${AdNameType.GDT.type}: ${error.errorCode}, ${error.errorMsg}")
            }

            override fun onADOpened() {}

            override fun onADExposure() {
                adListener.onAdPrepared(AdNameType.GDT.type)
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.exposure)}")
            }

            override fun onADClicked() {
                adListener.onAdClick(AdNameType.GDT.type)
                logd("${AdNameType.GDT.type}: ${activity.getString(R.string.clicked)}")
            }

            override fun onADLeftApplication() {}

            override fun onADClosed() {}
        })

        iad.loadAD()
    }

    private fun showAdInterBaiduMob(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {

        adListener.onStartRequest(AdNameType.BAIDU.type)
        adIntersContainer.setOnClickListener { adIntersContainer.visibility = View.GONE }
        adIntersContainer.visibility = View.GONE

        val interAd = InterstitialAd(activity, AdSize.InterstitialForVideoPausePlay, TogetherAd.idMapBaidu[adConstStr])

        val dm = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(dm)

        val n = ((if (dm.widthPixels > dm.heightPixels) dm.heightPixels else dm.widthPixels) * 0.8).toInt()
        interAd.loadAdForVideoApp(n, (n * 0.8).toInt())
        interAd.setListener(object : InterstitialAdListener {
            override fun onAdReady() {
                adIntersContainer.visibility = View.VISIBLE
                interAd.showAdInParentForVideoApp(activity, adIntersContainer)
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.show)}")
            }

            override fun onAdPresent() {
                adListener.onAdPrepared(AdNameType.BAIDU.type)
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.prepared)}")
            }

            override fun onAdClick(interstitialAd: InterstitialAd) {
                adListener.onAdClick(AdNameType.BAIDU.type)
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.clicked)}")
            }

            override fun onAdDismissed() {
                adIntersContainer.visibility = View.GONE
                adListener.onAdDismissed()
                interAd.loadAd()
                logd("${AdNameType.BAIDU.type}: ${activity.getString(R.string.dismiss)}")
            }

            override fun onAdFailed(s: String) {
                val newConfigStr = interConfigStr?.replace(AdNameType.BAIDU.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                loge("${AdNameType.BAIDU.type}: $s")
            }
        })
    }

    private fun showAdInterIFLY(
        @NonNull activity: Activity,
        interConfigStr: String?,
        @NonNull adConstStr: String,
        @NonNull isLandscape: Boolean,
        @NonNull adIntersContainer: RelativeLayout,
        @NonNull adListener: AdListenerInter
    ) {

        adListener.onStartRequest(AdNameType.XUNFEI.type)
        //创建插屏广告:adId:开发者在广告平台(http://www.voiceads.cn/)申请的广告位 ID
        val interstitialAd = IFLYInterstitialAd.createInterstitialAd(activity, TogetherAd.idMapXunFei[adConstStr])
        //点击手机后退键，是否销毁广告:"true":销毁，"false":不销毁，默认销毁 interstitialAd.setParameter(AdKeys.BACK_KEY_ENABLE, "true");
        //设置广告尺寸
        interstitialAd.setAdSize(IFLYAdSize.INTERSTITIAL)

        // 添加监听器，请求广告
        val mAdListener = object : IFLYAdListener {
            override fun onConfirm() {}

            override fun onCancel() {}

            override fun onAdReceive() {
                interstitialAd.showAd()
                adListener.onAdPrepared(AdNameType.XUNFEI.type)
                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.prepared)}")
            }

            override fun onAdFailed(adError: com.iflytek.voiceads.AdError) {
                val newConfigStr = interConfigStr?.replace(AdNameType.XUNFEI.type, AdNameType.NO.type)
                showAdInter(activity, newConfigStr, adConstStr, isLandscape, adIntersContainer, adListener)
                loge("${AdNameType.XUNFEI.type}: ${adError.errorCode}, ${adError.errorDescription}")
            }

            override fun onAdClick() {
                adListener.onAdClick(AdNameType.XUNFEI.type)
                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.clicked)}")
            }

            override fun onAdClose() {}

            override fun onAdExposure() {
                logd("${AdNameType.XUNFEI.type}: ${activity.getString(R.string.exposure)}")
            }
        }
        interstitialAd.loadAd(mAdListener)
    }

    interface AdListenerInter {

        fun onStartRequest(channel: String)

        fun onAdClick(channel: String)

        fun onAdFailed(failedMsg: String?)

        fun onAdDismissed()

        fun onAdPrepared(channel: String)
    }
}