package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.csj.TogetherAdCsj


/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderNativeExpress : CsjProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {

        callbackNativeExpressStartRequest(adProviderType, alias, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(CsjProvider.NativeExpress.supportDeepLink)
                .setAdCount(adCount)//请求广告数量为1到3条
                .setExpressViewAcceptedSize(CsjProvider.NativeExpress.expressViewAcceptedSizeWidth, CsjProvider.NativeExpress.expressViewAcceptedSizeHeight)//期望模板广告view的size,单位dp
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadNativeExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                if (ads.isNullOrEmpty()) {
                    callbackNativeExpressFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                ads.forEach { adObject ->
                    adObject.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
                        override fun onAdClicked(view: View?, type: Int) {
                            listener.onAdClicked(adProviderType, adObject)
                        }

                        override fun onAdShow(view: View?, type: Int) {
                            listener.onAdShow(adProviderType, adObject)
                        }

                        override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                            listener.onAdRenderSuccess(adProviderType, adObject)
                        }

                        override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
                            listener.onAdRenderFail(adProviderType, adObject)
                        }
                    })

                    adObject.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
                        override fun onSelected(position: Int, value: String?, enforce: Boolean) {
                            listener.onAdClosed(adProviderType, adObject)
                        }

                        override fun onCancel() {}
                        override fun onShow() {}
                    })
                }

                callbackNativeExpressLoaded(adProviderType, alias, listener, ads)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeExpressFailed(adProviderType, alias, listener, errorCode, errorMsg)
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