package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.mg.R

abstract class MgProviderBanner : BaseAdProvider() {

    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, alias, listener)
        callbackBannerFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun destroyBannerAd() {
        
    }
}