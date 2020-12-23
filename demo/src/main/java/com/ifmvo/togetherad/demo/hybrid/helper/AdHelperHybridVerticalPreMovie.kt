package com.ifmvo.togetherad.demo.hybrid.helper

import android.app.Activity
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.AdSlot
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.helper.BaseHelper
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.csj.provider.CsjProvider
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.native_.template.NativeTemplateSimple3
import org.jetbrains.annotations.NotNull
import java.lang.ref.WeakReference

/**
 * 广点通：激励、原生信息流
 *
 * 穿山甲：全屏视频
 *
 * Created by Matthew Chen on 2020/12/11.
 */
class AdHelperHybridVerticalPreMovie(

        @NotNull activity: Activity,
        @NotNull alias: String,
        @NotNull container: ViewGroup,
        ratioMap: LinkedHashMap<String, Int>? = null

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mContainer: ViewGroup = container
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var mListener: VerticalPreMovieListener? = null
    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String,
            @NotNull container: ViewGroup
    ) : this(activity, alias, container, null)

    fun loadAndShow(listener: VerticalPreMovieListener? = null) {
        onDestroy()

        mListener = listener

        val currentRatioMap: LinkedHashMap<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(mListener)
        reload(currentRatioMap)
    }

    private fun reload(@NotNull ratioMap: LinkedHashMap<String, Int>) {

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            mListener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            reload(filterType(ratioMap, adProviderType))
            return
        }

        when (adProviderType) {
            //广点通分为激励和原生
            AdProviderType.GDT.type -> {
//                realLoadReward(adProviderType, ratioMap)
                realLoadNative(adProviderType, ratioMap)
            }
            //穿山甲是全屏视频广告
            AdProviderType.CSJ.type -> {
                realLoadFullScreen(adProviderType, ratioMap)
            }
        }
    }

    private fun realLoadReward(adProviderType: String, ratioMap: LinkedHashMap<String, Int>) {
        adProvider?.requestRewardAd(mActivity.get()!!, adProviderType, mAlias, object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                reload(filterType(ratioMap, adProviderType))

                mListener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                if (mActivity.get() == null) {
                    reload(filterType(ratioMap, adProviderType))
                    mListener?.onAdFailed(providerType, "Activity为空")
                    return
                }
                cancelTimer()
                mListener?.onAdLoaded(providerType)

                adProvider?.showRewardAd(mActivity.get()!!)
            }

            override fun onAdExpose(providerType: String) {
                mListener?.onAdExpose(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }
        })
    }

    private fun realLoadFullScreen(adProviderType: String, ratioMap: LinkedHashMap<String, Int>) {
        adProvider?.requestFullVideoAd(mActivity.get()!!, adProviderType, mAlias, object : FullVideoListener {
            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                if (mActivity.get() == null) {
                    reload(filterType(ratioMap, adProviderType))
                    mListener?.onAdFailed(providerType, "Activity为空")
                    return
                }

                cancelTimer()
                mListener?.onAdLoaded(providerType)

                adProvider?.showFullVideoAd(mActivity.get()!!)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: String) {
                mListener?.onAdExpose(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }

            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                reload(filterType(ratioMap, adProviderType))

                mListener?.onAdFailed(providerType, failedMsg)
            }
        })
    }

    private var mAdObject: Any? = null
    private fun realLoadNative(adProviderType: String, ratioMap: LinkedHashMap<String, Int>) {
        CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
        adProvider?.getNativeAdList(mActivity.get()!!, adProviderType, mAlias, 1, object : NativeListener {
            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mListener?.onAdLoaded(providerType)

                mAdObject = adList[0]
                fun onDismiss(adProviderType: String) {
                    AdHelperNativePro.destroyAd(mAdObject)
                    mListener?.onAdClose(adProviderType)
                }

                AdHelperNativePro.show(adObject = mAdObject, container = mContainer, nativeTemplate = NativeTemplateSimple3(::onDismiss), listener = object : NativeViewListener {
                    override fun onAdExposed(providerType: String) {
                        mListener?.onAdExpose(providerType)
                    }

                    override fun onAdClicked(providerType: String) {
                        mListener?.onAdClicked(providerType)
                    }
                })
            }

            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                reload(filterType(ratioMap, adProviderType))

                mListener?.onAdFailed(providerType, failedMsg)
            }
        })
    }

    fun onResume() {
        AdHelperNativePro.resumeAd(mAdObject)
    }

    fun onPause() {
        AdHelperNativePro.pauseAd(mAdObject)
    }

    fun onDestroy() {
        AdHelperNativePro.destroyAd(mAdObject)
    }

    interface VerticalPreMovieListener : BaseListener {
        /**
         * 请求到了广告
         */
        fun onAdLoaded(@NotNull providerType: String) {}

        /**
         * 广告被点击了
         */
        fun onAdClicked(@NotNull providerType: String) {}

        /**
         * 广告曝光了（ 和 onAdShow 的区别是展示不一定曝光，曝光一定展示，需要展示一定的时间才会曝光，曝光的条件是提供商规定的 ）
         */
        fun onAdExpose(@NotNull providerType: String) {}

        /**
         * 广告被关闭了
         */
        fun onAdClose(@NotNull providerType: String) {}

    }
}