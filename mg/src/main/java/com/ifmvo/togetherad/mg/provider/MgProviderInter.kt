package com.ifmvo.togetherad.mg.provider

import android.app.Activity
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.mg.R

abstract class MgProviderInter : MgProviderFullVideo() {

    override fun requestInterAd(activity: Activity, adProviderType: String, alias: String, listener: InterListener) {
        callbackInterStartRequest(adProviderType, alias, listener)
        callbackInterFailed(adProviderType, alias, listener, null, activity.getString(R.string.mg_can_not))
    }

    override fun showInterAd(activity: Activity) {
    }

    override fun destroyInterAd() {
    }
}