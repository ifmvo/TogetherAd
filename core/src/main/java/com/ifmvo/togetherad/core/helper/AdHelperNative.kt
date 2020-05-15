package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
object AdHelperNative : BaseHelper() {

    private const val defaultMaxCount = 4

    fun getList(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, maxCount: Int = defaultMaxCount, listener: NativeListener? = null) {
        val currentMaxCount = if (maxCount <= 0) defaultMaxCount else maxCount
        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            listener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = AdHelperSplash.filterType(radioMap = currentRadioMap, adProviderType = adProviderType)
            getList(activity = activity, alias = alias, radioMap = newRadioMap, maxCount = maxCount, listener = listener)
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
                val newRadioMap = AdHelperSplash.filterType(radioMap = currentRadioMap, adProviderType = adProviderType)
                getList(activity = activity, alias = alias, radioMap = newRadioMap, maxCount = maxCount, listener = listener)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                listener?.onAdFailedAll(failedMsg)
            }
        })
    }

    fun show(@NonNull adObject: Any, @NonNull container: ViewGroup, nativeTemplate: BaseNativeTemplate) {
        TogetherAd.mProviders.entries.forEach { entry ->
            val adProvider = AdProviderLoader.loadAdProvider(entry.key)
            if (adProvider?.isBelongTheProvider(adObject) == true) {
                val nativeView = nativeTemplate.getNativeView(entry.key)
                nativeView?.showNative(adObject, container)
                return@forEach
            }
        }
    }
}