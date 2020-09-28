package com.ifmvo.togetherad.gdt

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.widget.NativeAdContainer

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtSimple1 : BaseNativeViewGdt() {

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_gdt_simple_1
    }

    override fun getNativeAdContainer(): NativeAdContainer? {
        return rootView?.findViewById(R.id.native_ad_container)
    }

    override fun getTitleTextView(): TextView? {
        return rootView?.findViewById(R.id.tv_title)
    }

    override fun getDescTextView(): TextView? {
        return rootView?.findViewById(R.id.tv_desc)
    }

    override fun getMediaView(): MediaView? {
        return rootView?.findViewById(R.id.gdt_media_view)
    }

    override fun getMainImageView(): ImageView? {
        return rootView?.findViewById(R.id.img_poster)
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        super.showNative(adProviderType, adObject, container, listener)

        getMainImageView()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
        getMediaView()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
    }
}