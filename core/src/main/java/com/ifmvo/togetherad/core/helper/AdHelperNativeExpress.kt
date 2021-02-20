package com.ifmvo.togetherad.core.helper

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.custom.express.BaseNativeExpressTemplate
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logw
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
class AdHelperNativeExpress(

        @NotNull activity: Activity,
        @NotNull alias: String,
        ratioMap: LinkedHashMap<String, Int>? = null,
        adCount: Int

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var mAdCount: Int = adCount
    private var adProvider: BaseAdProvider? = null

    //所有请求到的广告容器
    private var mAdList = mutableListOf<Any>()

    companion object {

        private const val defaultAdCount = 1

        fun show(@Nullable adObject: Any?, @Nullable container: ViewGroup?, @NotNull nativeExpressTemplate: BaseNativeExpressTemplate) {
            if (adObject == null) {
                return
            }
            if (container == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                if (adProvider?.nativeExpressAdIsBelongTheProvider(adObject) == true) {
                    val nativeView = nativeExpressTemplate.getNativeExpressView(entry.key)
                    nativeView?.showNativeExpress(entry.key, adObject, container)
                    return@forEach
                }
            }
        }

        fun destroyExpressAd(@Nullable adObject: Any?) {
            if (adObject == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeExpressAd(adObject)
            }
        }

        fun destroyExpressAd(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                return
            }
            adObjectList.forEach { destroyExpressAd(it) }
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

    fun getExpressList(listener: NativeExpressListener? = null) {
        val currentRatioMap: LinkedHashMap<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        getExpressListForMap(currentRatioMap, listener)
    }

    private fun getExpressListForMap(@NotNull ratioMap: LinkedHashMap<String, Int>, listener: NativeExpressListener? = null) {

        val currentAdCount = if (mAdCount <= 0) defaultAdCount else mAdCount

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            getExpressListForMap(filterType(ratioMap, adProviderType), listener)
            return
        }

        adProvider?.getNativeExpressAdList(mActivity.get()!!, adProviderType, mAlias, currentAdCount, object : NativeExpressListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                getExpressListForMap(filterType(ratioMap, adProviderType), listener)

                listener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mAdList.addAll(adList)
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdClicked(providerType: String, adObject: Any?) {
                listener?.onAdClicked(providerType, adObject)
            }

            override fun onAdShow(providerType: String, adObject: Any?) {
                listener?.onAdShow(providerType, adObject)
            }

            override fun onAdRenderSuccess(providerType: String, adObject: Any?) {
                listener?.onAdRenderSuccess(providerType, adObject)
            }

            override fun onAdRenderFail(providerType: String, adObject: Any?) {
                listener?.onAdRenderFail(providerType, adObject)
            }

            override fun onAdClosed(providerType: String, adObject: Any?) {
                listener?.onAdClosed(providerType, adObject)
            }
        })
    }

    /**
     * 销毁所有请求到的广告
     */
    fun destroyAllExpressAd() {
        destroyExpressAd(mAdList)
        mAdList.clear()
    }

}