package com.ifmvo.togetherad.csj

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple2 : BaseNativeViewCsj() {

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_csj_simple_2
    }

    override fun getImageContainer(): ViewGroup? {
        return rootView?.findViewById(R.id.ll_ad_container)
    }

    override fun getMainImageView_1(): ImageView? {
        return rootView?.findViewById(R.id.img_poster1)
    }

    override fun getMainImageView_2(): ImageView? {
        return rootView?.findViewById(R.id.img_poster2)
    }

    override fun getMainImageView_3(): ImageView? {
        return rootView?.findViewById(R.id.img_poster3)
    }

    override fun getVideoContainer(): ViewGroup? {
        return rootView?.findViewById(R.id.fl_ad_container)
    }

    override fun getTitleTextView(): TextView? {
        return rootView?.findViewById(R.id.tv_title)
    }

    override fun getDescTextView(): TextView? {
        return rootView?.findViewById(R.id.tv_desc)
    }
}