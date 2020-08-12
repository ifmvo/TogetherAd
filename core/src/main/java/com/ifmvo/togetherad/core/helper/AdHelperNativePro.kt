package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.support.annotation.NonNull
import android.support.annotation.Nullable
import android.view.ViewGroup
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import java.lang.ref.WeakReference

/**
 * 原生信息流广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperNativePro(

        @NonNull activity: Activity,
        @NonNull alias: String,
        radioMap: Map<String, Int>? = null,
        maxCount: Int

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRadioMap: Map<String, Int>? = radioMap
    private var mMaxCount: Int = maxCount
    private var adProvider: BaseAdProvider? = null

    companion object {

        private const val defaultMaxCount = 4

        fun show(@NonNull adObject: Any, @NonNull container: ViewGroup, @NonNull nativeTemplate: BaseNativeTemplate, @Nullable listener: NativeViewListener? = null) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                if (adProvider?.nativeAdIsBelongTheProvider(adObject) == true) {
                    val nativeView = nativeTemplate.getNativeView(entry.key)
                    nativeView?.showNative(entry.key, adObject, container, listener)
                    return@forEach
                }
            }
        }

        fun pauseAd(@NonNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.pauseNativeAd(adObject)
            }
        }

        fun pauseAd(@NonNull adObjectList: List<Any>) {
            adObjectList.forEach { pauseAd(it) }
        }

        fun resumeAd(@NonNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.resumeNativeAd(adObject)
            }
        }

        fun resumeAd(@NonNull adObjectList: List<Any>) {
            adObjectList.forEach { resumeAd(it) }
        }

        fun destroyAd(@NonNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeAd(adObject)
            }
        }

        fun destroyAd(@NonNull adObjectList: List<Any>) {
            adObjectList.forEach { destroyAd(it) }
        }
    }

    //为了照顾 Java 调用的同学
    constructor(
            @NonNull activity: Activity,
            @NonNull alias: String,
            maxCount: Int
    ) : this(activity, alias, null, maxCount)

    //为了照顾 Java 调用的同学
    constructor(
            @NonNull activity: Activity,
            @NonNull alias: String
    ) : this(activity, alias, null, defaultMaxCount)

    fun getList(listener: NativeListener? = null) {
        val currentRadioMap: Map<String, Int> = if (mRadioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else mRadioMap!!
        getListForMap(currentRadioMap, listener)
    }

    private fun getListForMap(@NonNull radioMap: Map<String, Int>, listener: NativeListener? = null) {

        val currentMaxCount = if (mMaxCount <= 0) defaultMaxCount else mMaxCount

        val adProviderType = AdRandomUtil.getRandomAdProvider(radioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            listener?.onAdFailedAll()
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            getListForMap(filterType(radioMap, adProviderType), listener)
            return
        }

        adProvider?.getNativeAdList(mActivity.get()!!, adProviderType, mAlias, currentMaxCount, object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                getListForMap(filterType(radioMap, adProviderType), listener)
            }

            override fun onAdFailedAll() {
                listener?.onAdFailedAll()
            }
        })
    }
}