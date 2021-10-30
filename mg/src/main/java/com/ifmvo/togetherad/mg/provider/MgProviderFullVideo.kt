package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.mg.R

abstract class MgProviderFullVideo : MgProviderBanner() {

    override fun requestFullVideoAd(activity: Activity, adProviderType: String, alias: String, listener: FullVideoListener) {
        callbackFullVideoStartRequest(adProviderType, alias, listener)
        callbackFullVideoFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun showFullVideoAd(activity: Activity): Boolean {
        return false
    }
}