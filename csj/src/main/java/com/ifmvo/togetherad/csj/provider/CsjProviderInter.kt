package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/2.
 */
abstract class CsjProviderInter : CsjProviderFullVideo() {

    private var mTTNativeExpressInterAd: TTNativeExpressAd? = null

    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {
        destroyInterAd()

        callbackInterStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(CsjProvider.Inter.supportDeepLink)
                .setExpressViewAcceptedSize(CsjProvider.Inter.expressViewAcceptedSizeWidth, CsjProvider.Inter.expressViewAcceptedSizeHeight)
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .setAdCount(1) //请求广告数量为1到3条
        TTAdSdk.getAdManager().createAdNative(activity).loadInteractionExpressAd(adSlotBuilder.build(), object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackInterFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }
                callbackInterLoaded(adProviderType, alias, listener)

                mTTNativeExpressInterAd = adList[0]
                mTTNativeExpressInterAd?.setExpressInteractionListener(object : TTNativeExpressAd.AdInteractionListener {
                    override fun onAdDismiss() {
                        callbackInterClosed(adProviderType, listener)
                    }

                    override fun onAdClicked(p0: View?, p1: Int) {
                        callbackInterClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, p1: Int) {
                        callbackInterExpose(adProviderType, listener)
                    }

                    override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                        mTTNativeExpressInterAd?.showInteractionExpressAd(activity)
                    }

                    override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {

                    }
                })
                mTTNativeExpressInterAd?.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(p0: Int, p1: String?, enforce: Boolean) {}
                    override fun onCancel() {}
                    override fun onShow() {}
                })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackInterFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun showInterAd(activity: Activity) {
        mTTNativeExpressInterAd?.render()
    }

    override fun destroyInterAd() {
        mTTNativeExpressInterAd?.destroy()
        mTTNativeExpressInterAd = null
    }

}