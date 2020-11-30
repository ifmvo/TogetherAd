package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2Template
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.AdRandomUtil
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logw
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
class AdHelperNativeExpress2(

        @NotNull activity: Activity,
        @NotNull alias: String,
        ratioMap: Map<String, Int>? = null,
        adCount: Int

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: Map<String, Int>? = ratioMap
    private var mAdCount: Int = adCount
    private var adProvider: BaseAdProvider? = null

    //所有请求到的广告容器
    private var mAdList = mutableListOf<Any>()

    companion object {

        private const val defaultAdCount = 1

        fun show(@NotNull activity: Activity, @Nullable adObject: Any?, @Nullable container: ViewGroup?, @NotNull nativeExpress2Template: BaseNativeExpress2Template, listener: NativeExpress2ViewListener) {
            if (adObject == null) {
                "adObject 广告对象不能为空".logw()
                return
            }
            if (container == null) {
                "container 广告容器不能为空".logw()
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                if (adProvider?.nativeExpress2AdIsBelongTheProvider(adObject) == true) {
                    val nativeView = nativeExpress2Template.getNativeExpress2View(entry.key)
                    nativeView?.showNativeExpress2(activity, entry.key, adObject, container, listener)
                    return@forEach
                }
            }
        }

        fun destroyExpress2Ad(@Nullable adObject: Any?) {
            if (adObject == null) {
                "adObject 广告对象不能为空".logw()
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeExpress2Ad(adObject)
            }
        }

        fun destroyExpress2Ad(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                "adObjectList 广告对象List不能为空".logw()
                return
            }
            adObjectList.forEach { destroyExpress2Ad(it) }
        }
    }

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String,
            adCount: Int
    ) : this(activity, alias, null, adCount)

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String
    ) : this(activity, alias, null, defaultAdCount)

    fun getExpress2List(listener: NativeExpress2Listener? = null) {
        val currentRatioMap: Map<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        getExpress2ListForMap(currentRatioMap, listener)
    }

    private fun getExpress2ListForMap(@NotNull ratioMap: Map<String, Int>, listener: NativeExpress2Listener? = null) {

        val currentAdCount = if (mAdCount <= 0) defaultAdCount else mAdCount

        val adProviderType = AdRandomUtil.getRandomAdProvider(ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll()
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            getExpress2ListForMap(filterType(ratioMap, adProviderType), listener)
            return
        }

        adProvider?.getNativeExpress2AdList(mActivity.get()!!, adProviderType, mAlias, currentAdCount, object : NativeExpress2Listener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                getExpress2ListForMap(filterType(ratioMap, adProviderType), listener)

                listener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mAdList.addAll(adList)
                listener?.onAdLoaded(providerType, adList)
            }
        })
    }

    /**
     * 销毁所有请求到的广告
     */
    fun destroyAllExpress2Ad() {
        destroyExpress2Ad(mAdList)
        mAdList.clear()
    }

}