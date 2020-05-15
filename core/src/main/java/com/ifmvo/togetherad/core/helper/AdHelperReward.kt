package com.ifmvo.togetherad.core.helper

import android.app.Activity
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperReward(

        @NonNull activity: Activity,
        @NonNull alias: String,
        radioMap: Map<String, Int>? = null,
        listener: RewardListener? = null

) : BaseHelper() {

    private var mActivity: Activity = activity
    private var mAlias: String = alias
    private var mRadioMap: Map<String, Int>? = radioMap
    private var mListener: RewardListener? = listener
    private var adProvider: BaseAdProvider? = null

    fun load() {
        val currentRadioMap: Map<String, Int> = if (mRadioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else mRadioMap!!
        reload(currentRadioMap)
    }

    private fun reload(@NonNull radioMap: Map<String, Int>) {

        val adProviderType = AdRandomUtil.getRandomAdProvider(radioMap)

        if (adProviderType?.isEmpty() != false) {
            mListener?.onAdFailedAll("配置中的广告全部加载失败，或配置中没有匹配的广告")
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            reload(filterType(radioMap, adProviderType))
            return
        }

        adProvider?.requestRewardAd(mActivity, adProviderType, mAlias, object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                mListener?.onAdFailed(providerType, failedMsg)
                reload(filterType(radioMap, adProviderType))
            }

            override fun onAdFailedAll(failedMsg: String?) {
                mListener?.onAdFailedAll(failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: String) {
                mListener?.onAdShow(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                mListener?.onAdLoaded(providerType)
            }

            override fun onAdExpose(providerType: String) {
                mListener?.onAdExpose(providerType)
            }

            override fun onAdVideoComplete(providerType: String) {
                mListener?.onAdVideoComplete(providerType)
            }

            override fun onAdVideoCached(providerType: String) {
                mListener?.onAdVideoCached(providerType)
            }

            override fun onAdRewardVerify(providerType: String) {
                mListener?.onAdRewardVerify(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }
        })
    }

    fun show() {
        adProvider?.showRewardAd(mActivity)
    }
}