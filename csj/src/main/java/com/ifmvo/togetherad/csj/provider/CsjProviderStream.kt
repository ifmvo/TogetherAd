package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.csj.TogetherAdCsj


/**
 * 自渲染贴片
 *
 * Created by Matthew_Chen on 2021.03.09.
 */
abstract class CsjProviderStream : CsjProviderSplash() {

    fun getStreamAdList(activity: Activity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

        callbackNativeStartRequest(adProviderType, alias, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setImageAcceptedSize(CsjProvider.Stream.imageAcceptedSizeWidth, CsjProvider.Stream.imageAcceptedSizeHeight)
                .setAdCount(maxCount)
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadStream(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(adList: MutableList<TTFeedAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackStreamFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackStreamLoaded(adProviderType, alias, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackStreamFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    fun resumeStreamAd(adObject: Any) {
        when (adObject) {
            is TTFeedAd -> {

            }
        }
    }

    fun pauseStreamAd(adObject: Any) {
        when (adObject) {
            is TTFeedAd -> {

            }
        }
    }

    fun destroyStreamAd(adObject: Any) {
        when (adObject) {
            is TTFeedAd -> {
                adObject.destroy()
            }
        }
    }

    fun streamAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTFeedAd
    }

}