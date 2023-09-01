package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.CountDownTimer
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.bytedance.sdk.openadsdk.TTAdNative.CSJSplashAdListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private var mTimer: CountDownTimer? = null

    private var mListener: SplashListener? = null
    private var mAdProviderType: String? = null

    private var mSplashAd: CSJSplashAd? = null
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
        adSlotBuilder.setAdLoadType(CsjProvider.Splash.adLoadType)

        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : CSJSplashAdListener {
            override fun onSplashLoadSuccess() {}

            override fun onSplashLoadFail(adError: CSJAdError?) {
                callbackSplashFailed(adProviderType, alias, listener, adError?.code, adError?.msg)
            }

            override fun onSplashRenderSuccess(splashAd: CSJSplashAd?) {
                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, alias, listener)

                mSplashAd = splashAd

                mSplashAd?.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                    override fun onSplashAdShow(splashAd: CSJSplashAd?) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onSplashAdClick(splashAd: CSJSplashAd?) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onSplashAdClose(splashAd: CSJSplashAd?, closeType: Int) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })
            }

            override fun onSplashRenderFail(splashAd: CSJSplashAd?, adError: CSJAdError?) {
                callbackSplashFailed(adProviderType, alias, listener, adError?.code, adError?.msg)
            }

        }, CsjProvider.Splash.maxFetchDelay)

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
            mSplashAd?.hideSkipButton()
//            mSplashAd?.setNotAllowSdkCountdown()
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

        adSlotBuilder.setAdLoadType(CsjProvider.Splash.adLoadType)

        TTAdSdk.getAdManager().createAdNative(activity).loadSplashAd(adSlotBuilder.build(), object : CSJSplashAdListener {
            override fun onSplashLoadSuccess() {}

            override fun onSplashLoadFail(adError: CSJAdError?) {
                callbackSplashFailed(adProviderType, alias, listener, adError?.code, adError?.msg)
            }

            override fun onSplashRenderSuccess(splashAd: CSJSplashAd?) {
                if (splashAd == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求成功，但是返回的广告为null")
                    return
                }

                callbackSplashLoaded(adProviderType, alias, listener)

                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                    override fun onSplashAdShow(splashAd: CSJSplashAd?) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onSplashAdClick(splashAd: CSJSplashAd?) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onSplashAdClose(splashAd: CSJSplashAd?, closeType: Int) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })

                //自定义跳过按钮和计时逻辑
                if (customSkipView != null) {
                    splashAd.hideSkipButton()
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

            override fun onSplashRenderFail(splashAd: CSJSplashAd?, adError: CSJAdError?) {
                callbackSplashFailed(adProviderType, alias, listener, adError?.code, adError?.msg)
            }

        }, CsjProvider.Splash.maxFetchDelay)
    }

}