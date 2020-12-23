package com.ifmvo.togetherad.demo.hybrid.helper

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.R
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.helper.AdHelperNativeExpress
import com.ifmvo.togetherad.core.helper.AdHelperNativeExpress2
import com.ifmvo.togetherad.core.helper.BaseHelper
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logw
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.native_.template.NativeExpress2TemplateSimple
import com.ifmvo.togetherad.demo.native_.template.NativeExpressTemplateSimple
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

/**
 *
 * Created by Matthew Chen on 2020/12/1.
 */
class AdHelperHybridExpress(

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

        fun show(@Nullable adProviderType: String?, @NotNull activity: Activity, @Nullable adObject: Any?, @Nullable container: ViewGroup?) {
            if (adObject == null) {
                "adObject 广告对象不能为空".logw()
                return
            }
            if (container == null) {
                "container 广告容器不能为空".logw()
                return
            }

            when (adProviderType) {
                AdProviderType.GDT.type -> {
                    AdHelperNativeExpress.show(adObject, container, NativeExpressTemplateSimple())
                }
                AdProviderType.CSJ.type -> {
                    AdHelperNativeExpress2.show(activity, adObject, container, NativeExpress2TemplateSimple(), object : NativeExpress2ViewListener {
                        override fun onAdExposed(providerType: String) {

                        }

                        override fun onAdClicked(providerType: String) {

                        }

                        override fun onAdRenderSuccess(providerType: String) {

                        }

                        override fun onAdRenderFailed(providerType: String) {

                        }

                        override fun onAdClose(providerType: String) {

                        }
                    })
                }
            }

        }

        fun destroyHybridExpressAd(@Nullable adObject: Any?) {
            if (adObject == null) {
                "adObject 广告对象不能为空".logw()
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeExpressAd(adObject)
                adProvider?.destroyNativeExpress2Ad(adObject)
            }
        }

        fun destroyHybridExpressAd(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                "adObjectList 广告对象List不能为空".logw()
                return
            }
            adObjectList.forEach { destroyHybridExpressAd(it) }
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

    fun getHybridExpressList(listener: NativeExpress2Listener? = null) {
        val currentRatioMap = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        getHybridExpressListForMap(currentRatioMap, listener)
    }

    private fun getHybridExpressListForMap(@NotNull ratioMap: LinkedHashMap<String, Int>, listener: NativeExpress2Listener? = null) {

        val currentAdCount = if (mAdCount <= 0) defaultAdCount else mAdCount

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            getHybridExpressListForMap(filterType(ratioMap, adProviderType), listener)
            return
        }

        when (adProviderType) {
            AdProviderType.GDT.type -> {
                getHybridExpressList(adProviderType, currentAdCount, listener, ratioMap)
            }
            AdProviderType.CSJ.type -> {
                getHybridExpress2List(adProviderType, currentAdCount, listener, ratioMap)
            }
        }
    }

    private fun getHybridExpressList(adProviderType: String, currentAdCount: Int, listener: NativeExpress2Listener?, ratioMap: LinkedHashMap<String, Int>) {
        adProvider?.getNativeExpressAdList(mActivity.get()!!, adProviderType, mAlias, currentAdCount, object : NativeExpressListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                getHybridExpressListForMap(filterType(ratioMap, adProviderType), listener)

                listener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mAdList.addAll(adList)
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdClicked(providerType: String, adObject: Any?) {

            }

            override fun onAdShow(providerType: String, adObject: Any?) {

            }

            override fun onAdRenderSuccess(providerType: String, adObject: Any?) {

            }

            override fun onAdRenderFail(providerType: String, adObject: Any?) {

            }

            override fun onAdClosed(providerType: String, adObject: Any?) {

            }
        })
    }

    private fun getHybridExpress2List(adProviderType: String, currentAdCount: Int, listener: NativeExpress2Listener?, ratioMap: LinkedHashMap<String, Int>) {
        adProvider?.getNativeExpress2AdList(mActivity.get()!!, adProviderType, mAlias, currentAdCount, object : NativeExpress2Listener {
            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mAdList.addAll(adList)
                listener?.onAdLoaded(providerType, adList)

            }

            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                getHybridExpressListForMap(filterType(ratioMap, adProviderType), listener)

                listener?.onAdFailed(providerType, failedMsg)
            }
        })
    }

    /**
     * 销毁所有请求到的广告
     */
    fun destroyAllHybridExpressAd() {
        destroyHybridExpressAd(mAdList)
        mAdList.clear()
    }

}