package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.ks.R
import com.ifmvo.togetherad.ks.TogetherAdKs
import com.kwad.sdk.api.KsLoadManager
import com.kwad.sdk.api.KsNativeAd
import com.kwad.sdk.api.KsScene

abstract class KsProviderNative : KsProviderInter() {

    override fun getNativeAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        callbackNativeStartRequest(adProviderType, alias, listener)

        if (TogetherAdKs.adRequestManager == null) {
            callbackNativeFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val ksScene = KsScene.Builder(TogetherAdKs.idMapKs[alias] ?: 0).build()

        TogetherAdKs.adRequestManager!!.loadNativeAd(ksScene, object : KsLoadManager.NativeAdListener {
            override fun onNativeAdLoad(adList: MutableList<KsNativeAd>?) {
                //list是空的，按照错误来处理
                if (adList?.isEmpty() != false) {
                    callbackNativeFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeLoaded(adProviderType, alias, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is KsNativeAd
    }

    override fun resumeNativeAd(adObject: Any) {
    }

    override fun pauseNativeAd(adObject: Any) {
    }

    override fun destroyNativeAd(adObject: Any) {
    }
}