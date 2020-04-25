package com.ifmvo.togetherad.csj

import android.view.View
import android.view.ViewGroup
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjCommon : BaseNativeView() {

    override fun showNative(adObject: Any, container: ViewGroup) {
        val rootView = View.inflate(container.context, R.layout.layout_native_view_csj_common, container)

    }

}