package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/2.
 */
abstract class CsjProviderBanner : BaseAdProvider() {

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        destroyBannerAd()

        callbackBannerStartRequest(adProviderType, alias, listener)

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(CsjProvider.Banner.supportDeepLink)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(CsjProvider.Banner.expressViewAcceptedSizeWidth, CsjProvider.Banner.expressViewAcceptedSizeHeight)
                .setImageAcceptedSize(640, 320)//这个参数设置即可，不影响个性化模板广告的size
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadBannerExpressAd(adSlot, object : TTAdNative.NativeExpressAdListener {
            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackBannerFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                mTTNativeExpressBannerAd = adList[0]
                mTTNativeExpressBannerAd?.setSlideIntervalTime(CsjProvider.Banner.slideIntervalTime)
                mTTNativeExpressBannerAd?.setExpressInteractionListener(object : TTNativeExpressAd.ExpressAdInteractionListener {
                    override fun onAdClicked(p0: View?, p1: Int) {
                        callbackBannerClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, p1: Int) {
                        callbackBannerExpose(adProviderType, listener)
                    }

                    override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
                        callbackBannerLoaded(adProviderType, alias, listener)
                        container.addView(view)
                    }

                    override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
                        destroyBannerAd()
                        callbackBannerFailed(adProviderType, alias, listener, errorCode, errorMsg)
                    }
                })
                mTTNativeExpressBannerAd?.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String) {
                        //用户选择不喜欢原因后，移除广告展示
                        container.removeAllViews()
                        destroyBannerAd()
                        callbackBannerClosed(adProviderType, listener)
                    }

                    override fun onCancel() {}
                    override fun onRefuse() {}
                    override fun onShow() {}
                })
                mTTNativeExpressBannerAd?.render()
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                destroyBannerAd()
                callbackBannerFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    private var mTTNativeExpressBannerAd: TTNativeExpressAd? = null

    override fun destroyBannerAd() {
        mTTNativeExpressBannerAd?.destroy()
        mTTNativeExpressBannerAd = null
    }

}