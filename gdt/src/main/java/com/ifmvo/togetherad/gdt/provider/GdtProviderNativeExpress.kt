package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize
import com.qq.e.ads.nativ.NativeExpressAD
import com.qq.e.ads.nativ.NativeExpressADView
import com.qq.e.comm.util.AdError

/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderNativeExpress : GdtProviderNative() {

    override fun getNativeExpressAdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpressListener) {

        callbackNativeExpressStartRequest(adProviderType, alias, listener)

        val nativeExpressADListener = object : NativeExpressAD.NativeExpressADListener {

            override fun onADLoaded(ads: MutableList<NativeExpressADView>?) {
                if (ads.isNullOrEmpty()) {
                    callbackNativeExpressFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeExpressLoaded(adProviderType, alias, listener, ads)
            }

            override fun onNoAD(adError: AdError?) {
                callbackNativeExpressFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            override fun onRenderSuccess(adView: NativeExpressADView?) {
                callbackNativeExpressRenderSuccess(adProviderType, adView, listener)
            }

            override fun onRenderFail(adView: NativeExpressADView?) {
                callbackNativeExpressRenderFail(adProviderType, adView, listener)
            }

            override fun onADClicked(adView: NativeExpressADView?) {
                callbackNativeExpressClicked(adProviderType, adView, listener)
            }

            override fun onADExposure(adView: NativeExpressADView?) {
                callbackNativeExpressShow(adProviderType, adView, listener)
            }

            override fun onADClosed(adView: NativeExpressADView?) {
                callbackNativeExpressClosed(adProviderType, adView, listener)
            }

            override fun onADLeftApplication(adView: NativeExpressADView?) {}
        }

        val nativeExpressAD = NativeExpressAD(activity, ADSize(GdtProvider.NativeExpress.adWidth, GdtProvider.NativeExpress.adHeight), TogetherAdGdt.idMapGDT[alias], nativeExpressADListener)
        nativeExpressAD.setVideoOption(VideoOption.Builder()
                .setAutoPlayPolicy(GdtProvider.NativeExpress.autoPlayPolicy)
                .setAutoPlayMuted(GdtProvider.NativeExpress.autoPlayMuted)
                .build())
        nativeExpressAD.setMinVideoDuration(GdtProvider.NativeExpress.minVideoDuration)
        nativeExpressAD.setMaxVideoDuration(GdtProvider.NativeExpress.maxVideoDuration)
        nativeExpressAD.loadAD(adCount)
    }

    override fun destroyNativeExpressAd(adObject: Any) {
        if (adObject !is NativeExpressADView) return
        adObject.destroy()
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeExpressADView
    }
}