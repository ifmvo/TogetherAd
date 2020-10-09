package com.ifmvo.togetherad.core.helper

import android.app.Activity
import org.jetbrains.annotations.NotNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.core.utils.loge

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperSplash : BaseHelper() {

    @Deprecated(
            message = "设计上考虑不周全，gdt的自定义跳过按钮不应该放在通用helper中，请及时使用 GdtProvider.Splash.customSkipView 进行替换",
            replaceWith = ReplaceWith(expression = "GdtProvider.Splash.customSkipView", imports = ["com.ifmvo.togetherad.gdt"])
    )
    var customSkipView: BaseSplashSkipView? = null

    //为了照顾 Java 调用的同学
    fun show(@NotNull activity: Activity, @NotNull alias: String, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        show(activity, alias, null, container, listener)
    }

    fun show(@NotNull activity: Activity, @NotNull alias: String, ratioMap: Map<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        startTimer(listener)
        realShow(activity, alias, ratioMap, container, listener)
    }

    private fun realShow(@NotNull activity: Activity, @NotNull alias: String, ratioMap: Map<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        val currentRatioMap = if (ratioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else ratioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRatioMap)

        if (adProviderType?.isEmpty() != false) {
            customSkipView = null
            cancelTimer()
            listener?.onAdFailedAll()
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${activity.getString(R.string.no_init)}".loge()
            val newRatioMap = filterType(currentRatioMap, adProviderType)
            realShow(activity, alias, newRatioMap, container, listener)
            return
        }

        adProvider.showSplashAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                listener?.onAdFailed(providerType, failedMsg)
                val newRatioMap = filterType(currentRatioMap, adProviderType)
                realShow(activity, alias, newRatioMap, container, listener)
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
                customSkipView = null
                listener?.onAdDismissed(providerType)
            }
        })
    }
}