package com.ifmvo.togetherad.demo.extend

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.csj.provider.CsjProvider

/**
 *
 * Created by Matthew Chen on 2020/10/23.
 */
class CustomCsjProvider : CsjProvider() {

    object Banner {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 257

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    /**
     * 这里只重写相应的方法即可
     */
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, alias, listener)

        destroyBannerAd()

        val adSlot = AdSlot.Builder()
                .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
                .setSupportDeepLink(Banner.supportDeepLink)
                .setImageAcceptedSize(Banner.imageAcceptedSizeWidth, Banner.imageAcceptedSizeHeight)
                .build()

        TTAdSdk.getAdManager().createAdNative(activity).loadBannerAd(adSlot, object : TTAdNative.BannerAdListener {
            override fun onBannerAdLoad(bannerAd: TTBannerAd?) {
                if (bannerAd == null) {
                    callbackBannerFailed(adProviderType, alias, listener, null, "请求成功，但是返回的 bannerAd 为空")
                    return
                }

                val bannerView = bannerAd.bannerView
                if (bannerView == null) {
                    callbackBannerFailed(adProviderType, alias, listener, null, "请求成功，但是返回的 bannerView 为空")
                    return
                }

                callbackBannerLoaded(adProviderType, alias, listener)

                bannerAd.setSlideIntervalTime(Banner.slideIntervalTime)
                container.removeAllViews()
                container.addView(bannerView)

                bannerAd.setBannerInteractionListener(object : TTBannerAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, type: Int) {
                        callbackBannerClicked(adProviderType, listener)
                    }

                    override fun onAdShow(view: View?, type: Int) {
                        callbackBannerExpose(adProviderType, listener)
                    }
                })

                bannerAd.setShowDislikeIcon(object : TTAdDislike.DislikeInteractionCallback {
                    override fun onSelected(position: Int, value: String?) {
                        container.removeAllViews()
                        callbackBannerClosed(adProviderType, listener)
                    }

                    override fun onCancel() {}
                    override fun onRefuse() {}
                    override fun onShow() {}
                })
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                "onError".loge(tag)
                callbackBannerFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

}