package com.ifmvo.togetherad.csj

import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
class CsjProvider : BaseAdProvider() {

    private val adProviderType = AdProviderType.CSJ

    override fun showSplashAd(activity: Activity, alias: String, radio: String?, container: ViewGroup, listener: SplashListener) {

        callbackStartRequest(adProviderType, listener)

        val wm = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            wm.defaultDisplay.getRealSize(point)
        } else {
            wm.defaultDisplay.getSize(point)
        }
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(true)
                .setImageAcceptedSize(point.x, point.y)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackFailed(adProviderType, listener, "请求成功，但是返回的广告为null")
                    return
                }

                callbackLoaded(adProviderType, listener)

                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        callbackDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        callbackDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onTimeout() {
                callbackFailed(adProviderType, listener, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackFailed(adProviderType, listener, "错误码：$errorCode, 错误信息：$errorMsg")
            }
        }, 2500)//超时时间，demo 为 2000
    }

    override fun getNativeAdList(activity: Activity, alias: String, radio: String?, listener: SplashListener) {

    }

}