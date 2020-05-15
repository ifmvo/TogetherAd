package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperSplash : BaseHelper() {

    var customSkipView: BaseSplashSkipView? = null

    fun show(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, @NonNull container: ViewGroup, listener: SplashListener? = null) {

        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            customSkipView = null
            listener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = filterType(currentRadioMap, adProviderType)
            show(activity, alias, newRadioMap, container, listener)
            return
        }

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

            override fun onAdFailedAll(failedMsg: String?) {
                customSkipView = null
                listener?.onAdFailedAll(failedMsg)
            }

            override fun onAdDismissed(providerType: String) {
                customSkipView = null
                listener?.onAdDismissed(providerType)
            }
        })
    }
}