package com.ifmvo.togetherad.baidu.native_.view

import android.view.ViewGroup
import com.ifmvo.togetherad.baidu.R
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewBaiduSimple2 : BaseNativeViewBaidu() {

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_baidu_simple_2
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        super.showNative(adProviderType, adObject, container, listener)
        //主图 16:9
        getMainImageView()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) / 3 * 9 / 16)
    }
}