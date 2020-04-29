package com.ifmvo.togetherad.core.helper

import android.app.Activity
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperReward : BaseHelper() {

    private var adProvider: BaseAdProvider? = null

    fun load(@NonNull activity: Activity, @NonNull alias: String, radioMap: Map<String, Int>? = null, listener: RewardListener? = null) {
        val currentRadioMap = if (radioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else radioMap

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadioMap)

        if (adProviderType?.isEmpty() != false) {
            listener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            val newRadioMap = filterType(currentRadioMap, adProviderType)
            load(activity, alias, newRadioMap, listener)
            return
        }

        adProvider?.requestRewardAd(activity, adProviderType, alias, object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadioMap = filterType(currentRadioMap, adProviderType)
                load(activity, alias, newRadioMap, listener)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                listener?.onAdFailedAll(failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: String) {
                listener?.onAdShow(providerType)
            }

            override fun onAdLoaded(providerType: String) {
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

    fun show(@NonNull activity: Activity) {
        adProvider?.showRewardAd(activity)
    }
}