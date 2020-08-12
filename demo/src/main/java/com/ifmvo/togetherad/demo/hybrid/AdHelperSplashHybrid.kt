package com.ifmvo.togetherad.demo.hybrid

import android.app.Activity
import android.support.annotation.NonNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.helper.BaseHelper
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.demo.AdProviderType

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperSplashHybrid : BaseHelper() {

    var customSkipView: BaseSplashSkipView? = null

    //为了照顾 Java 调用的同学
    fun show(@NonNull activity: Activity, @NonNull alias: String, @NonNull container: ViewGroup, listener: SplashListener? = null) {
        show(activity, alias, null, container, listener)
    }

    fun show(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, @NonNull container: ViewGroup, listener: SplashListener? = null) {

        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            customSkipView = null
            listener?.onAdFailedAll()
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = filterType(currentRadioMap, adProviderType)
            show(activity, alias, newRadioMap, container, listener)
            return
        }

        when (adProviderType) {
            AdProviderType.BAIDU.type, AdProviderType.CSJ.type -> {
                showSplash(adProvider, activity, adProviderType, alias, container, listener, currentRadioMap)
            }
            AdProviderType.GDT.type -> {
                showNative(adProvider, activity, adProviderType, alias, listener, container, currentRadioMap)
            }
        }
    }

    private var mAdObject: Any? = null
    private fun showNative(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, listener: SplashListener?, container: ViewGroup, currentRadioMap: Map<String, Int>) {
        adProvider.getNativeAdList(activity = activity, adProviderType = adProviderType, alias = alias, maxCount = 0, listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                listener?.onAdLoaded(providerType)

                mAdObject = adList[0]
                AdHelperNativePro.show(adObject = adList[0], container = container, nativeTemplate = NativeTemplateSplash {
                    customSkipView = null
                    listener?.onAdDismissed(it)
                }, listener = object : NativeViewListener {
                    override fun onAdExposed(providerType: String) {
                        listener?.onAdExposure(providerType)
                    }

                    override fun onAdClicked(providerType: String) {
                        listener?.onAdClicked(providerType)
                    }
                })
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                show(activity, alias, newRadioMap, container, listener)
            }

            override fun onAdFailedAll() {
                customSkipView = null
                listener?.onAdFailedAll()
            }
        })
    }

    private fun showSplash(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener?, currentRadioMap: Map<String, Int>) {
        adProvider.showSplashAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                show(activity, alias, newRadioMap, container, listener)
            }

            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                listener?.onAdLoaded(providerType)
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdExposure(providerType: String) {
                listener?.onAdExposure(providerType)
            }

            override fun onAdFailedAll() {
                customSkipView = null
                listener?.onAdFailedAll()
            }

            override fun onAdDismissed(providerType: String) {
                customSkipView = null
                listener?.onAdDismissed(providerType)
            }
        })
    }

    fun resumeAd() {
        mAdObject?.run {
            AdHelperNativePro.resumeAd(this)
        }
    }

    fun pauseAd() {
        mAdObject?.run {
            AdHelperNativePro.pauseAd(this)
        }
    }

    fun destroyAd() {
        customSkipView = null
        mAdObject?.run {
            AdHelperNativePro.destroyAd(this)
            mAdObject = null
        }
    }

}