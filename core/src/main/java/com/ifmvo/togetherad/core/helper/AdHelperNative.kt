package com.ifmvo.togetherad.core.helper

import android.app.Activity
import org.jetbrains.annotations.NotNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.core.utils.loge
import org.jetbrains.annotations.Nullable

/**
 * 原生自渲染广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
@Deprecated(message = "设计上考虑不周全，使用单例导致无法同时请求多次", replaceWith = ReplaceWith(expression = "AdHelperNativePro", imports = ["com.ifmvo.togetherad.core.helper"]))
object AdHelperNative : BaseHelper() {

    private const val defaultMaxCount = 4

    //为了照顾 Java 调用的同学
    fun getList(@NotNull activity: Activity, @NotNull alias: String, maxCount: Int = defaultMaxCount, listener: NativeListener? = null) {
        getList(activity, alias, null, maxCount, listener)
    }

    fun getList(@NotNull activity: Activity, @NotNull alias: String, ratioMap: Map<String, Int>? = null, maxCount: Int = defaultMaxCount, listener: NativeListener? = null) {
        val currentMaxCount = if (maxCount <= 0) defaultMaxCount else maxCount
        val currentRatioMap = if (ratioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else ratioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRatioMap)

        if (adProviderType?.isEmpty() != false) {
            listener?.onAdFailedAll()
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${activity.getString(R.string.no_init)}".loge()
            val newRatioMap = filterType(ratioMap = currentRatioMap, adProviderType = adProviderType)
            getList(activity = activity, alias = alias, ratioMap = newRatioMap, maxCount = maxCount, listener = listener)
            return
        }

        adProvider.getNativeAdList(activity = activity, adProviderType = adProviderType, alias = alias, maxCount = currentMaxCount, listener = object : NativeListener {

            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRatioMap = filterType(ratioMap = currentRatioMap, adProviderType = adProviderType)
                getList(activity = activity, alias = alias, ratioMap = newRatioMap, maxCount = maxCount, listener = listener)
            }
        })
    }

    fun show(@NotNull adObject: Any, @NotNull container: ViewGroup, @NotNull nativeTemplate: BaseNativeTemplate, @Nullable listener: NativeViewListener? = null) {
        TogetherAd.mProviders.entries.forEach { entry ->
            val adProvider = AdProviderLoader.loadAdProvider(entry.key)
            if (adProvider?.nativeAdIsBelongTheProvider(adObject) == true) {
                val nativeView = nativeTemplate.getNativeView(entry.key)
                nativeView?.showNative(entry.key, adObject, container, listener)
                return@forEach
            }
        }
    }
}