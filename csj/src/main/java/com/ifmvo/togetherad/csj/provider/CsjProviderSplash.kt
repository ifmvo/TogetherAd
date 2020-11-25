package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private var mSplashAd: TTSplashAd? = null
    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, listener)
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Splash.supportDeepLink)
                .setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, listener)

                mSplashAd = splashAd

                mSplashAd?.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, listener, null, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (mSplashAd?.splashView == null) {
            return false
        }

        container.removeAllViews()
        container.addView(mSplashAd!!.splashView)
        return true
    }

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, listener)
        //step3:创建开屏广告请求参数AdSlot,具体参数含义参考文档
        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Splash.supportDeepLink)
                .setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, listener)

                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, listener, null, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

}