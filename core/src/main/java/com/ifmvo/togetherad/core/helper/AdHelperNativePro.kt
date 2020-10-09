package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.core.utils.loge
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

/**
 * 原生信息流广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperNativePro(

        @NotNull activity: Activity,
        @NotNull alias: String,
        ratioMap: Map<String, Int>? = null,
        maxCount: Int

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: Map<String, Int>? = ratioMap
    private var mMaxCount: Int = maxCount
    private var adProvider: BaseAdProvider? = null

    companion object {

        @Deprecated(
                message = "设计上考虑不周全，csj的api不应该放在通用helper中，请及时使用 CsjProvider.Native.nativeAdType 进行替换",
                replaceWith = ReplaceWith(expression = "CsjProvider.Native.nativeAdType", imports = ["com.ifmvo.togetherad.csj"])
        )
        var csjNativeAdType: Int = -1

        private const val defaultMaxCount = 4

        fun show(@NotNull adObject: Any, @NotNull container: ViewGroup, @NotNull nativeTemplate: BaseNativeTemplate, @Nullable listener: NativeViewListener? = null) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                if (adProvider?.nativeAdIsBelongTheProvider(adObject) == true) {
                    val nativeView = nativeTemplate.getNativeView(entry.key)
                    nativeView?.showNative(entry.key, adObject, container, listener)
                    return@forEach
                }
            }
        }

        fun pauseAd(@NotNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.pauseNativeAd(adObject)
            }
        }

        fun pauseAd(@NotNull adObjectList: List<Any>) {
            adObjectList.forEach { pauseAd(it) }
        }

        fun resumeAd(@NotNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.resumeNativeAd(adObject)
            }
        }

        fun resumeAd(@NotNull adObjectList: List<Any>) {
            adObjectList.forEach { resumeAd(it) }
        }

        fun destroyAd(@NotNull adObject: Any) {
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeAd(adObject)
            }
        }

        fun destroyAd(@NotNull adObjectList: List<Any>) {
            adObjectList.forEach { destroyAd(it) }
        }
    }

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String,
            maxCount: Int
    ) : this(activity, alias, null, maxCount)

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String
    ) : this(activity, alias, null, defaultMaxCount)

    fun getList(listener: NativeListener? = null) {
        val currentRatioMap: Map<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        getListForMap(currentRatioMap, listener)
    }

    private fun getListForMap(@NotNull ratioMap: Map<String, Int>, listener: NativeListener? = null) {

        val currentMaxCount = if (mMaxCount <= 0) defaultMaxCount else mMaxCount

        val adProviderType = AdRandomUtil.getRandomAdProvider(ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll()
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            getListForMap(filterType(ratioMap, adProviderType), listener)
            return
        }

        adProvider?.getNativeAdList(mActivity.get()!!, adProviderType, mAlias, currentMaxCount, object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                listener?.onAdFailed(providerType, failedMsg)
                getListForMap(filterType(ratioMap, adProviderType), listener)
            }
        })
    }
}