package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdDislike.DislikeInteractionCallback
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTNativeExpressAd
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.csj.TogetherAdCsj


/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderNativeExpress : CsjProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {

        callbackNativeExpressStartRequest(adProviderType, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(CsjProvider.NativeExpress.supportDeepLink)
                .setAdCount(adCount)//请求广告数量为1到3条
                .setExpressViewAcceptedSize(CsjProvider.NativeExpress.expressViewAcceptedSizeWidth, CsjProvider.NativeExpress.expressViewAcceptedSizeHeight)//期望模板广告view的size,单位dp
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                if (ads.isNullOrEmpty()) {
                    callbackNativeExpressFailed(adProviderType, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                ads.forEach { adObject ->
                    adObject.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(view: View?, type: Int) {
                            callbackNativeExpressClicked(adProviderType, adObject, listener)
                        }

                        override fun onAdShow(view: View?, type: Int) {
                            callbackNativeExpressShow(adProviderType, adObject, listener)
                        }

                        override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                            callbackNativeExpressRenderSuccess(adProviderType, adObject, listener)
                        }

                        override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
                            callbackNativeExpressRenderFail(adProviderType, adObject, listener)
                        }
                    })

                    adObject.setDislikeCallback(activity, object : DislikeInteractionCallback {
                        override fun onSelected(position: Int, value: String) {
                            callbackNativeExpressClosed(adProviderType, adObject, listener)
                        }

                        override fun onCancel() {}
                        override fun onRefuse() {}
                    })
                }

                callbackNativeExpressLoaded(adProviderType, listener, ads)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeExpressFailed(adProviderType, listener, errorCode, errorMsg)
            }
        })
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        if (adObject !is TTNativeExpressAd) return
        adObject.destroy()
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTNativeExpressAd
    }
}