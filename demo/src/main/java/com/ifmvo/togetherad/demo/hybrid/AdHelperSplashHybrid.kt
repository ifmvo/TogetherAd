package com.ifmvo.togetherad.demo.hybrid

import android.app.Activity
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.helper.BaseHelper
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.native_.NativeTemplateSimple3
import org.jetbrains.annotations.NotNull

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperSplashHybrid : BaseHelper() {

    //为了照顾 Java 调用的同学
    fun show(@NotNull activity: Activity, @NotNull alias: String, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        show(activity, alias, null, container, listener)
    }

    fun show(@NotNull activity: Activity, @NotNull alias: String, radioMap: Map<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        startTimer(listener)
        realShow(activity, alias, radioMap, container, listener)
    }

    private fun realShow(@NotNull activity: Activity, @NotNull alias: String, radioMap: Map<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            cancelTimer()
            listener?.onAdFailedAll()
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = filterType(currentRadioMap, adProviderType)
            show(activity, alias, newRadioMap, container, listener)
            return
        }

        //可以在这里修改，哪种平台使用开屏，哪种平台使用原生
        when (adProviderType) {
            AdProviderType.CSJ.type, AdProviderType.GDT.type -> {
                showNative(adProvider, activity, adProviderType, alias, container, listener, currentRadioMap)
            }
            AdProviderType.BAIDU.type -> {
                showSplash(adProvider, activity, adProviderType, alias, container, listener, currentRadioMap)
            }
        }
    }

    private var mAdObject: Any? = null
    private fun showNative(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener?, currentRadioMap: Map<String, Int>) {
        CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
        adProvider.getNativeAdList(activity = activity, adProviderType = adProviderType, alias = alias, maxCount = 1, listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                listener?.onAdLoaded(providerType)

                fun onDismiss(adProviderType: String) {
                    listener?.onAdDismissed(adProviderType)
                }

                mAdObject = adList[0]
                AdHelperNativePro.show(adObject = adList[0], container = container, nativeTemplate = NativeTemplateSimple3(::onDismiss), listener = object : NativeViewListener {
                    override fun onAdExposed(providerType: String) {
                        listener?.onAdExposure(providerType)
                    }

                    override fun onAdClicked(providerType: String) {
                        listener?.onAdClicked(providerType)
                    }
                })
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                show(activity, alias, newRadioMap, container, listener)
            }

        })
    }

    private fun showSplash(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener?, currentRadioMap: Map<String, Int>) {
        adProvider.showSplashAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                show(activity, alias, newRadioMap, container, listener)
            }

            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                cancelTimer()
                listener?.onAdLoaded(providerType)
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdExposure(providerType: String) {
                listener?.onAdExposure(providerType)
            }

            override fun onAdDismissed(providerType: String) {
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
        mAdObject?.run {
            AdHelperNativePro.destroyAd(this)
            mAdObject = null
        }
    }

}