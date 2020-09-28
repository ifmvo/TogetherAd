package com.ifmvo.togetherad.csj

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple1 : BaseNativeViewCsj() {

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_csj_simple_1
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