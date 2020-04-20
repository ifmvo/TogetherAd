package com.ifmvo.togetherad.core.helper

import android.app.Activity
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.FlowListener
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
object AdHelperFlow : BaseHelper() {

    private const val defaultMaxCount = 4

    fun getList(activity: Activity, alias: String, radio: String?, maxCount: Int = defaultMaxCount, listener: FlowListener?) {
        val currentRadio = if (radio?.isEmpty() != false) TogetherAd.getDefaultProviderRadio() else radio
        val currentMaxCount = if (maxCount <= 0) defaultMaxCount else maxCount

        val adProviderType = AdRandomUtil.getRandomAdProvider(currentRadio)

        if (adProviderType == AdProviderType.NO) {
            listener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)
                ?: throw Exception("随机到的广告商没注册，请检查初始化代码")

        adProvider.getNativeAdList(activity, alias, radio, currentMaxCount, object : FlowListener {

            override fun onAdStartRequest(providerType: AdProviderType) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: AdProviderType, adList: List<*>) {
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdFailed(providerType: AdProviderType, failedMsg: String?) {
                listener?.onAdFailed(providerType, failedMsg)
                val newRadio = currentRadio.replace(providerType.type, AdProviderType.NO.type)
                getList(activity, alias, newRadio, currentMaxCount, listener)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                listener?.onAdFailedAll(failedMsg)
            }
        })
    }

}