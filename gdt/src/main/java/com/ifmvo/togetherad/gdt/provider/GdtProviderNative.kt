package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.nativ.NativeADUnifiedListener
import com.qq.e.ads.nativ.NativeUnifiedAD
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderNative : GdtProviderInter() {

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

        callbackNativeStartRequest(adProviderType, alias, listener)

        val nativeADUnifiedListener = object : NativeADUnifiedListener {
            override fun onADLoaded(adList: List<NativeUnifiedADData>?) {
                //list是空的，按照错误来处理
                if (adList?.isEmpty() != false) {
                    callbackNativeFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeLoaded(adProviderType, alias, listener, adList)
            }

            override fun onNoAD(adError: AdError?) {
                callbackNativeFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }
        }

        val mAdManager = NativeUnifiedAD(activity, TogetherAdGdt.idMapGDT[alias], nativeADUnifiedListener)
        mAdManager.setDownAPPConfirmPolicy(GdtProvider.Native.downAPPConfirmPolicy)
        GdtProvider.Native.categories?.let { mAdManager.setCategories(it) }
        mAdManager.setMaxVideoDuration(GdtProvider.Native.maxVideoDuration)//有效值就是 5-60
        mAdManager.setMinVideoDuration(GdtProvider.Native.minVideoDuration)
        mAdManager.loadData(maxCount)
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeUnifiedADData
    }

    override fun resumeNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        adObject.resume()
        if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {
            adObject.resumeVideo()
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {
            adObject.pauseVideo()
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        if (adObject !is NativeUnifiedADData) return
        adObject.destroy()
    }

}