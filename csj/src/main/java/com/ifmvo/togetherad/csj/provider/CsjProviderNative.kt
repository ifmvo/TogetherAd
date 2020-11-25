package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderNative : CsjProviderInter() {

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        if (CsjProvider.Native.nativeAdType == -1) {
            throw IllegalArgumentException(
                    """
    |-------------------------------------------------------------------------------------- 
    |  必须在每次请求穿山甲的原生广告之前设置类型。
    |  设置方式：
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_INTERACTION_AD
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_BANNER
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_CACHED_SPLASH
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_DRAW_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_REWARD_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_SPLASH
    |  任选其一
    |--------------------------------------------------------------------------------------

"""
            )
        }

        callbackNativeStartRequest(adProviderType, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Native.supportDeepLink)
                .setImageAcceptedSize(CsjProvider.Native.imageAcceptedSizeWidth, CsjProvider.Native.imageAcceptedSizeHeight)
                .setNativeAdType(CsjProvider.Native.nativeAdType)
                .setAdCount(maxCount)
                .build()
        TTAdSdk.getAdManager().createAdNative(activity).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onNativeAdLoad(adList: MutableList<TTNativeAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackNativeFailed(adProviderType, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeLoaded(adProviderType, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeFailed(adProviderType, listener, errorCode, errorMsg)
            }
        })
    }

    override fun resumeNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTNativeAd
    }

}