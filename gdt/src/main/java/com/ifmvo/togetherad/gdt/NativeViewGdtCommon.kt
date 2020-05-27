package com.ifmvo.togetherad.gdt

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.NativeADEventListener
import com.qq.e.ads.nativ.NativeADMediaListener
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtCommon : BaseNativeView() {

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {

        if (adObject !is NativeUnifiedADData) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_gdt_common, container)

        val mNativeAdContainer = rootView.findViewById<NativeAdContainer>(R.id.native_ad_container)
        val mLlSuper = rootView.findViewById<LinearLayout>(R.id.ll_super)
        val mImgPoster = rootView.findViewById<ImageView>(R.id.img_poster)
        val mTvTitle = rootView.findViewById<TextView>(R.id.tv_title)
        val mTvDesc = rootView.findViewById<TextView>(R.id.tv_desc)
        val mAdGdtMediaPlayer = rootView.findViewById<MediaView>(R.id.gdt_media_view)

        mImgPoster?.layoutParams?.height = ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16
        mAdGdtMediaPlayer?.layoutParams?.height = ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16

        mTvTitle?.text = adObject.title
        mTvDesc?.text = adObject.desc
        when (adObject.adPatternType) {
            AdPatternType.NATIVE_2IMAGE_2TEXT, AdPatternType.NATIVE_3IMAGE -> {
                mAdGdtMediaPlayer.visibility = View.GONE
                mImgPoster.visibility = View.VISIBLE
                TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster, adObject.imgUrl)
            }
            AdPatternType.NATIVE_VIDEO -> {
                mAdGdtMediaPlayer.visibility = View.VISIBLE
                mImgPoster.visibility = View.GONE
            }
        }
        val clickableViews = arrayListOf<View>()
        clickableViews.add(mLlSuper)
        clickableViews.add(mImgPoster)
        clickableViews.add(mAdGdtMediaPlayer)
        adObject.bindAdToView(container.context, mNativeAdContainer, null, clickableViews)
        adObject.setNativeAdEventListener(object : NativeADEventListener {
            override fun onADStatusChanged() {
            }

            override fun onADError(error: AdError?) {
            }

            override fun onADClicked() {
                listener?.onAdClicked(adProviderType)
            }

            override fun onADExposed() {
                listener?.onAdExposed(adProviderType)
            }
        })
        when (adObject.adPatternType) {
            AdPatternType.NATIVE_VIDEO -> {
                val videoOption = VideoOption.Builder().setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build()
                adObject.bindMediaView(mAdGdtMediaPlayer, videoOption, object : NativeADMediaListener {
                    override fun onVideoInit() {}
                    override fun onVideoStop() {}
                    override fun onVideoPause() {}
                    override fun onVideoStart() {}
                    override fun onVideoError(p0: AdError?) {}
                    override fun onVideoCompleted() {}
                    override fun onVideoLoading() {}
                    override fun onVideoReady() {}
                    override fun onVideoLoaded(p0: Int) {}
                    override fun onVideoClicked() {}
                    override fun onVideoResume() {}
                })
                adObject.startVideo()
            }
        }
    }

}