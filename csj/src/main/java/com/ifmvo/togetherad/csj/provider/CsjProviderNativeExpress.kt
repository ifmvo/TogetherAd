package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.csj.TogetherAdCsj


/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderNativeExpress : CsjProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeExpressListener) {

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(CsjProvider.NativeExpress.supportDeepLink)
                .setAdCount(maxCount) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(CsjProvider.NativeExpress.expressViewAcceptedSizeWidth, CsjProvider.NativeExpress.expressViewAcceptedSizeHeight)//期望模板广告view的size,单位dp
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                if (ads.isNullOrEmpty()) {
                    callbackNativeExpressFailed(adProviderType, listener, null, "请求成功，但是返回的list为空")
                    return
                }
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeExpressFailed(adProviderType, listener, errorCode, errorMsg)
            }
        })
    }

    override fun resumeNativeExpressAd(adObject: Any) {
        when (adObject) {
            is TTNativeExpressAd -> {

            }
        }
    }

    override fun pauseNativeExpressAd(adObject: Any) {
        when (adObject) {
            is TTNativeExpressAd -> {

            }
        }
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        when (adObject) {
            is TTNativeExpressAd -> {

            }
        }
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTNativeExpressAd
    }
}