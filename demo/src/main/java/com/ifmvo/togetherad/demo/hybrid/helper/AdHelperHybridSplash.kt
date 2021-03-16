package com.ifmvo.togetherad.demo.hybrid.helper

import android.app.Activity
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.helper.BaseHelper
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.csj.provider.CsjProvider
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.native_.template.NativeTemplateSimple3
import org.jetbrains.annotations.NotNull
import java.lang.ref.WeakReference

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
class AdHelperHybridSplash(

        @NotNull activity: Activity,
        @NotNull alias: String,
        ratioMap: LinkedHashMap<String, Int>? = null

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var adProvider: BaseAdProvider? = null
    private var mListener: SplashListener? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String
    ) : this(activity, alias, null)

    //为了照顾 Java 调用的同学
    fun loadOnly(listener: SplashListener? = null) {
        destroyAd()

        mListener = listener
        val currentRatioMap = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        realLoadOnly(currentRatioMap)
    }

    private fun realLoadOnly(@NotNull ratioMap: LinkedHashMap<String, Int>) {

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            mListener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            mListener = null
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            realLoadOnly(filterType(ratioMap, adProviderType))
            return
        }

        //可以在这里修改，哪种平台使用开屏，哪种平台使用原生
        when (adProviderType) {
            AdProviderType.GDT.type -> {
                realLoadOnlySplash(ratioMap, adProviderType)
            }
            AdProviderType.BAIDU.type, AdProviderType.CSJ.type -> {
                realLoadOnlyNative(ratioMap, adProviderType)
            }
        }

    }

    private var mAdObject: Any? = null
    private fun realLoadOnlyNative(ratioMap: LinkedHashMap<String, Int>, adProviderType: String) {
        CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
        adProvider?.getNativeAdList(activity = mActivity.get()!!, adProviderType = adProviderType, alias = mAlias, maxCount = 1, listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mListener?.onAdLoaded(providerType)

                mAdObject = adList[0]
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                mListener?.onAdFailed(providerType, failedMsg)
                val newRatioMap = filterType(ratioMap, adProviderType)
                realLoadOnly(newRatioMap)
            }
        })
    }

    private fun realLoadOnlySplash(ratioMap: LinkedHashMap<String, Int>, adProviderType: String) {
        adProvider?.loadOnlySplashAd(activity = mActivity.get()!!, adProviderType = adProviderType, alias = mAlias, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                mListener?.onAdFailed(providerType, failedMsg)
                val newRatioMap = filterType(ratioMap, adProviderType)
                realLoadOnly(newRatioMap)
            }

            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                cancelTimer()
                mListener?.onAdLoaded(providerType)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdExposure(providerType: String) {
                mListener?.onAdExposure(providerType)
            }

            override fun onAdDismissed(providerType: String) {
                mListener?.onAdDismissed(providerType)
                mListener = null
            }
        })
    }

    fun showAd(@NotNull container: ViewGroup): Boolean {

        if (mAdObject != null) {
            fun onClose(adProviderType: String) {
                mListener?.onAdDismissed(adProviderType)
                destroyAd()
            }

            AdHelperNativePro.show(adObject = mAdObject, container = container, nativeTemplate = NativeTemplateSimple3(::onClose), listener = object : NativeViewListener {
                override fun onAdExposed(providerType: String) {
                    mListener?.onAdExposure(providerType)
                }

                override fun onAdClicked(providerType: String) {
                    mListener?.onAdClicked(providerType)
                }
            })
            return true
        } else {
            return adProvider?.showSplashAd(container) ?: false
        }
    }

    fun resumeAd() {
        mAdObject?.run {
            AdHelperNativePro.resumeAd(this)
        }
    }

    fun pauseAd() {
        mAdObject?.run {
            AdHelperNativePro.pauseAd(this)
        }
    }

    fun destroyAd() {
        mAdObject?.run {
            AdHelperNativePro.destroyAd(this)
            mAdObject = null
            mListener = null
        }
    }
}