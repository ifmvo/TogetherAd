package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import org.jetbrains.annotations.NotNull

/**
 * 激励广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperRewardPro : BaseHelper() {

    //为了照顾 Java 调用的同学
    fun show(@NotNull activity: Activity, @NotNull alias: String, listener: RewardListener? = null) {
        show(activity, alias, null, listener)
    }

    fun show(@NotNull activity: Activity, @NotNull alias: String, ratioMap: LinkedHashMap<String, Int>? = null, listener: RewardListener? = null) {
        startTimer(listener)
        realShow(activity, alias, ratioMap, listener)
    }

    private fun realShow(@NotNull activity: Activity, @NotNull alias: String, ratioMap: LinkedHashMap<String, Int>? = null, listener: RewardListener? = null) {
        val currentRatioMap = if (ratioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else ratioMap

        val adProviderType = DispatchUtil.getAdProvider(alias, currentRatioMap)

        if (adProviderType?.isEmpty() != false) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${activity.getString(R.string.no_init)}".loge()
            val newRatioMap = filterType(currentRatioMap, adProviderType)
            realShow(activity, alias, newRatioMap, listener)
            return
        }

        adProvider.requestAndShowRewardAd(activity = activity, adProviderType = adProviderType, alias = alias,  listener = object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                realShow(activity, alias, ratioMap, listener)

                listener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: String) {
                listener?.onAdShow(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                cancelTimer()
                listener?.onAdLoaded(providerType)
            }

            override fun onAdExpose(providerType: String) {
                listener?.onAdExpose(providerType)
            }

            override fun onAdVideoComplete(providerType: String) {
                listener?.onAdVideoComplete(providerType)
            }

            override fun onAdVideoCached(providerType: String) {
                listener?.onAdVideoCached(providerType)
            }

            override fun onAdRewardVerify(providerType: String) {
                listener?.onAdRewardVerify(providerType)
            }

            override fun onAdClose(providerType: String) {
                listener?.onAdClose(providerType)
            }
        })
    }
}