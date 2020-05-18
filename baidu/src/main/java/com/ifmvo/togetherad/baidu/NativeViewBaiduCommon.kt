package com.ifmvo.togetherad.baidu

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.mobad.feeds.NativeResponse
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.utils.ScreenUtil

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewBaiduCommon : BaseNativeView() {

    override fun showNative(adObject: Any, container: ViewGroup) {
        if (adObject !is NativeResponse) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_baidu_common, container)

        val mImgPoster = rootView.findViewById<ImageView>(R.id.img_poster)
        val mLlParent = rootView.findViewById<LinearLayout>(R.id.ll_parent)
        val mTvTitle = rootView.findViewById<TextView>(R.id.tv_title)
        val mTvDesc = rootView.findViewById<TextView>(R.id.tv_desc)
//        val mAdLogoView = rootView.findViewById<AdLogoView>(R.id.ad_logo_view)
        val layoutParams = mImgPoster?.layoutParams
        layoutParams?.height = ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16

        mTvTitle?.text = adObject.title
        mTvDesc?.text = adObject.desc
//        mAdLogoView.setAdLogoType(AdNameType.BAIDU, adObject)
        TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster, adObject.imageUrl)
//        ILFactory.getLoader().load(mContext, mImgPoster, adObject.imageUrl, LoaderOptions().placeHolder(R.mipmap.ic_hot_placeholder).error(R.mipmap.ic_hot_placeholder).skipCache())
        mLlParent?.setOnClickListener {
            adObject.handleClick(it)
//            ClickEvent.adPolyClick(mContext, umengLocationStr, currentAdChannel)
        }
    }
}