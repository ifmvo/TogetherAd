package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.mg.R

abstract class MgProviderSplash : MgProviderReward() {

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        return false
    }

}