//package com.ifmvo.togetherad.demo.splash
//
//import android.app.Activity
//import android.support.annotation.NonNull
//import android.view.ViewGroup
//import com.ifmvo.togetherad.core.TogetherAd
//import com.ifmvo.togetherad.core.config.AdProviderLoader
//import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
//import com.ifmvo.togetherad.core.helper.AdHelperNativePro
//import com.ifmvo.togetherad.core.helper.AdHelperSplash
//import com.ifmvo.togetherad.core.helper.BaseHelper
//import com.ifmvo.togetherad.core.listener.NativeListener
//import com.ifmvo.togetherad.core.listener.NativeViewListener
//import com.ifmvo.togetherad.core.listener.SplashListener
//import com.ifmvo.togetherad.core.provider.BaseAdProvider
//import com.ifmvo.togetherad.core.utils.AdRandomUtil
//import com.ifmvo.togetherad.demo.AdProviderType
//import com.ifmvo.togetherad.demo.native_.NativeTemplateCommon
//
///**
// * 开屏广告
// *
// * Created by Matthew Chen on 2020-04-03.
// */
//object SplashAdapter : BaseHelper() {
//
//    var customSkipView: BaseSplashSkipView? = null
//
//    //为了照顾 Java 调用的同学
//    fun show(@NonNull activity: Activity, @NonNull alias: String, @NonNull container: ViewGroup, listener: SplashListener? = null) {
//        show(activity, alias, null, container, listener)
//    }
//
//    fun show(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, @NonNull container: ViewGroup, listener: SplashListener? = null) {
//
//        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap
//
//        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)
//
//        if (adProviderType?.isEmpty() != false) {
//            customSkipView = null
//            listener?.onAdFailedAll()
//            return
//        }
//
//        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)
//
//        if (adProvider == null) {
//            val newRadioMap = AdHelperSplash.filterType(currentRadioMap, adProviderType)
//            show(activity, alias, newRadioMap, container, listener)
//            return
//        }
//
//        when (adProviderType) {
//            AdProviderType.BAIDU.type -> {
//                showSplash(adProvider, activity, adProviderType, alias, container, listener, currentRadioMap)
//            }
//            AdProviderType.CSJ.type, AdProviderType.GDT.type -> {
//                showNative(adProvider, activity, adProviderType, alias, listener, container, currentRadioMap)
//            }
//        }
//    }
//
//    private fun showNative(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, listener: SplashListener?, container: ViewGroup, currentRadioMap: Map<String, Int>) {
//        adProvider.getNativeAdList(activity = activity, adProviderType = adProviderType, alias = alias, maxCount = 0, listener = object : NativeListener {
//            override fun onAdStartRequest(providerType: String) {
//                listener?.onAdStartRequest(providerType)
//            }
//
//            override fun onAdLoaded(providerType: String, adList: List<Any>) {
//                listener?.onAdLoaded(providerType)
//
//                AdHelperNativePro.show(adObject = adList[0], container = container, nativeTemplate = NativeTemplateCommon(), listener = object : NativeViewListener {
//                    override fun onAdExposed(providerType: String) {
//                        listener?.onAdExposure(providerType)
//                    }
//
//                    override fun onAdClicked(providerType: String) {
//                        listener?.onAdClicked(providerType)
//                    }
//                })
//            }
//
//            override fun onAdFailed(providerType: String, failedMsg: String?) {
//                listener?.onAdFailed(providerType, failedMsg)
//                val newRadioMap = AdHelperSplash.filterType(currentRadioMap, adProviderType)
//                show(activity, alias, newRadioMap, container, listener)
//            }
//
//            override fun onAdFailedAll() {
//                listener?.onAdFailedAll()
//            }
//        })
//    }
//
//    private fun showSplash(adProvider: BaseAdProvider, activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener?, currentRadioMap: Map<String, Int>) {
//        adProvider.showSplashAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : SplashListener {
//            override fun onAdFailed(providerType: String, failedMsg: String?) {
//                listener?.onAdFailed(providerType, failedMsg)
//                val newRadioMap = AdHelperSplash.filterType(currentRadioMap, adProviderType)
//                show(activity, alias, newRadioMap, container, listener)
//            }
//
//            override fun onAdStartRequest(providerType: String) {
//                listener?.onAdStartRequest(providerType)
//            }
//
//            override fun onAdLoaded(providerType: String) {
//                listener?.onAdLoaded(providerType)
//            }
//
//            override fun onAdClicked(providerType: String) {
//                listener?.onAdClicked(providerType)
//            }
//
//            override fun onAdExposure(providerType: String) {
//                listener?.onAdExposure(providerType)
//            }
//
//            override fun onAdFailedAll() {
//                customSkipView = null
//                listener?.onAdFailedAll()
//            }
//
//            override fun onAdDismissed(providerType: String) {
//                customSkipView = null
//                listener?.onAdDismissed(providerType)
//            }
//        })
//    }
//
//}