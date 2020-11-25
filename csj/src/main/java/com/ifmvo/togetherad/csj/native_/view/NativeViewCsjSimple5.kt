package com.ifmvo.togetherad.csj.native_.view

import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.csj.native_.view.BaseNativeViewCsj

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple5 : BaseNativeViewCsj(){

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        super.showNative(adProviderType, adObject, container, listener)

        getImageContainer()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
        getVideoContainer()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
    }

}