package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.support.annotation.NonNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/**
 * Banner 横幅广告
 *
 * Created by Matthew Chen on 2020/5/25.
 */
object AdHelperBanner : BaseHelper() {

    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    fun show(@NonNull activity: Activity, @NonNull alias: String, @NonNull container: ViewGroup, listener: BannerListener? = null) {
        show(activity, alias, null, container, listener)
    }

    fun show(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, @NonNull container: ViewGroup, listener: BannerListener? = null) {

        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            listener?.onAdFailedAll()
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = filterType(currentRadioMap, adProviderType)
            show(activity, alias, newRadioMap, container, listener)
            return
        }

        adProvider?.showBannerAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : BannerListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(adProviderType)
            }

            override fun onAdLoaded(providerType: String) {
                listener?.onAdLoaded(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                show(activity, alias, newRadioMap, container, listener)
            }

            override fun onAdFailedAll() {
                listener?.onAdFailedAll()
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdExpose(providerType: String) {
                listener?.onAdExpose(providerType)
            }

            override fun onAdClose(providerType: String) {
                listener?.onAdClose(providerType)
            }
        })
    }

    fun destroy() {
        adProvider?.destroyBannerAd()
        adProvider = null
    }
}