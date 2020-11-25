package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.banner2.UnifiedBannerADListener
import com.qq.e.ads.banner2.UnifiedBannerView
import com.qq.e.comm.util.AdError

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderBanner : BaseAdProvider() {

    private var banner: UnifiedBannerView? = null
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, listener)
        destroyBannerAd()
        banner = UnifiedBannerView(activity, TogetherAdGdt.idMapGDT[alias], object : UnifiedBannerADListener {
            override fun onADCloseOverlay() {
                "onADCloseOverlay".logi(TAG)
            }

            override fun onADExposure() {
                callbackBannerExpose(adProviderType, listener)
            }

            override fun onADClosed() {
                callbackBannerClosed(adProviderType, listener)
            }

            override fun onADLeftApplication() {
                "onADLeftApplication".logi(TAG)
            }

            override fun onADOpenOverlay() {
                "onADOpenOverlay".logi(TAG)
            }

            override fun onNoAD(adError: AdError?) {
                banner?.destroy()
                callbackBannerFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            override fun onADReceive() {
                callbackBannerLoaded(adProviderType, listener)
            }

            override fun onADClicked() {
                callbackBannerClicked(adProviderType, listener)
            }
        })
        container.addView(banner)
        banner?.loadAD()
    }

    override fun destroyBannerAd() {
        banner?.destroy()
        banner = null
    }

}