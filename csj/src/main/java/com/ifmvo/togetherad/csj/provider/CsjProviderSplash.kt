package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTSplashAd
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj
import kotlin.math.roundToInt

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private var mTimer: CountDownTimer? = null

    private var mListener: SplashListener? = null
    private var mAdProviderType: String? = null

    private var mSplashAd: TTSplashAd? = null
    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {

        mListener = listener
        mAdProviderType = adProviderType

        callbackSplashStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        if (CsjProvider.Splash.isExpress) {
            adSlotBuilder.setExpressViewAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth.toFloat(), CsjProvider.Splash.imageAcceptedSizeHeight.toFloat())
        } else {
            adSlotBuilder.setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, alias, listener)

                mSplashAd = splashAd

                mSplashAd?.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (mSplashAd?.splashView == null) {
            return false
        }

        container.removeAllViews()
        container.addView(mSplashAd!!.splashView)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        if (customSkipView != null) {
            mSplashAd?.setNotAllowSdkCountdown()
            skipView?.run {
                container.addView(this, customSkipView.getLayoutParams())
                setOnClickListener {
                    mTimer?.cancel()
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }
            }

            //开始倒计时
            mTimer?.cancel()
            mTimer = object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    val second = (millisUntilFinished / 1000f).roundToInt()
                    customSkipView.handleTime(second)
                }
            }
            mTimer?.start()
        }

        return true
    }

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(activity)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        if (CsjProvider.Splash.isExpress) {
            adSlotBuilder.setExpressViewAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth.toFloat(), CsjProvider.Splash.imageAcceptedSizeHeight.toFloat())
        } else {
            adSlotBuilder.setImageAcceptedSize(CsjProvider.Splash.imageAcceptedSizeWidth, CsjProvider.Splash.imageAcceptedSizeHeight)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, alias, listener)

                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })

                //自定义跳过按钮和计时逻辑
                if (customSkipView != null) {
                    splashAd.setNotAllowSdkCountdown()
                    skipView?.run {
                        container.addView(this, customSkipView.getLayoutParams())
                        setOnClickListener {
                            mTimer?.cancel()
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    }

                    //开始倒计时
                    mTimer?.cancel()
                    mTimer = object : CountDownTimer(5000, 1000) {
                        override fun onFinish() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            val second = (millisUntilFinished / 1000f).roundToInt()
                            customSkipView.handleTime(second)
                        }
                    }
                    mTimer?.start()
                }
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

}