package com.ifmvo.togetherad.core.helper

import android.app.Activity
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
object AdHelperReward : BaseHelper() {

    fun load(@NonNull activity: Activity, @NonNull alias: String, radio: String? = null, listener: RewardListener? = null) {
        val currentRadio = if (radio?.isEmpty() != false) TogetherAd.getDefaultProviderRadio() else radio

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadio)

        if (adProviderType == AdProviderType.NO) {
            listener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)
                ?: throw Exception("随机到的广告商没注册，请检查初始化代码")

        adProvider.requestRewardAd(activity, alias, object : RewardListener {
            override fun onAdStartRequest(providerType: AdProviderType) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: AdProviderType, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadio = currentRadio.replace(providerType.type, AdProviderType.NO.type)
                load(activity, alias, newRadio, listener)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                listener?.onAdFailedAll(failedMsg)
            }

            override fun onAdClicked(providerType: AdProviderType) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: AdProviderType) {
                listener?.onAdShow(providerType)
            }
        })
    }

}