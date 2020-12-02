package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.nativ.express2.NativeExpressAD2
import com.qq.e.ads.nativ.express2.NativeExpressADData2
import com.qq.e.ads.nativ.express2.VideoOption2
import com.qq.e.comm.util.AdError


/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderNativeExpress2 : GdtProviderNativeExpress() {

    override fun getNativeExpress2AdList(activity: Activity, adProviderType: String, alias: String, adCount: Int, listener: NativeExpress2Listener) {

        callbackNativeExpressStartRequest(adProviderType, alias, listener)

        val adLoaderListener = object : NativeExpressAD2.AdLoadListener {
            override fun onLoadSuccess(ads: MutableList<NativeExpressADData2>?) {
                if (ads.isNullOrEmpty()) {
                    callbackNativeExpressFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }
                callbackNativeExpressLoaded(adProviderType, alias, listener, ads)
            }

            override fun onNoAD(adError: AdError?) {
                callbackNativeExpressFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }
        }

        val mNativeExpressAD2 = NativeExpressAD2(activity, TogetherAdGdt.idMapGDT[alias], adLoaderListener)
        mNativeExpressAD2.setAdSize(GdtProvider.NativeExpress.adWidth, GdtProvider.NativeExpress.adHeight) // 单位dp
        mNativeExpressAD2.setDownAPPConfirmPolicy(GdtProvider.NativeExpress.downAPPConfirmPolicy)
        //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认），SDK 按照默认逻辑选择
        mNativeExpressAD2.setBrowserType(GdtProvider.NativeExpress.browserType)
        // 如果您在平台上新建原生模板广告位时，选择了支持视频，那么可以进行个性化设置（可选）
        val builder = VideoOption2.Builder()
        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前设置setAutoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         */
        builder.setAutoPlayPolicy(GdtProvider.NativeExpress.autoPlayPolicyVideoOption2) // WIFI 环境下可以自动播放视频
                .setAutoPlayMuted(GdtProvider.NativeExpress.autoPlayMuted) // 自动播放时为静音
                .setDetailPageMuted(GdtProvider.NativeExpress.detailPageMuted) // 视频详情页播放时不静音
                .setMaxVideoDuration(GdtProvider.NativeExpress.maxVideoDuration) // 设置返回视频广告的最大视频时长（闭区间，可单独设置），单位:秒，默认为 0 代表无限制，合法输入为：5<=maxVideoDuration<=60. 此设置会影响广告填充，请谨慎设置
                .setMinVideoDuration(GdtProvider.NativeExpress.minVideoDuration) // 设置返回视频广告的最小视频时长（闭区间，可单独设置），单位:秒，默认为 0 代表无限制， 此设置会影响广告填充，请谨慎设置

        mNativeExpressAD2.setVideoOption2(builder.build())
        mNativeExpressAD2.loadAd(adCount)

    }

    override fun destroyNativeExpress2Ad(adObject: Any) {
        if (adObject !is NativeExpressADData2) return
        adObject.destroy()
    }

    override fun nativeExpress2AdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is NativeExpressADData2
    }
}