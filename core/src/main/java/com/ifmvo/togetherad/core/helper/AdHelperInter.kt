package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.support.annotation.NonNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import java.lang.ref.WeakReference

/**
 * 激励广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperInter(

        @NonNull activity: Activity,
        @NonNull alias: String,
        radioMap: Map<String, Int>? = null,
        listener: InterListener? = null

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRadioMap: Map<String, Int>? = radioMap
    private var mListener: InterListener? = listener
    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NonNull activity: Activity,
            @NonNull alias: String,
            listener: InterListener? = null
    ) : this(activity, alias, null, listener)

    fun load() {
        val currentRadioMap: Map<String, Int> = if (mRadioMap?.isEmpty() != false) TogetherAd.getPublicProviderRadio() else mRadioMap!!
        reload(currentRadioMap)
    }

    private fun reload(@NonNull radioMap: Map<String, Int>) {

        val adProviderType = AdRandomUtil.getRandomAdProvider(radioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            mListener?.onAdFailedAll()
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            reload(filterType(radioMap, adProviderType))
            return
        }

        adProvider?.requestInterAd(mActivity.get()!!, adProviderType, mAlias, object : InterListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                mListener?.onAdLoaded(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                mListener?.onAdFailed(providerType, failedMsg)
                reload(filterType(radioMap, adProviderType))
            }

            override fun onAdFailedAll() {
                mListener?.onAdFailedAll()
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdExpose(providerType: String) {
                mListener?.onAdExpose(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }
        })
    }

    fun show() {
        adProvider?.showInterAd(mActivity.get()!!)
    }
}