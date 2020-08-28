package com.ifmvo.togetherad.gdt

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.core.utils.logd
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
class NativeViewGdtSimple2 : BaseNativeView() {

    private val TAG = "NativeViewGdtSimple2"

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {

        if (adObject !is NativeUnifiedADData) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_gdt_simple_2, container)

        val mNativeAdContainer = rootView.findViewById<NativeAdContainer>(R.id.native_ad_container)
        val mMediaContainer = rootView.findViewById<FrameLayout>(R.id.media_container)
        val mImgPoster = rootView.findViewById<ImageView>(R.id.img_poster)
        val mTvTitle = rootView.findViewById<TextView>(R.id.tv_title)
        val mTvDesc = rootView.findViewById<TextView>(R.id.tv_desc)
        val mAdGdtMediaPlayer = rootView.findViewById<MediaView>(R.id.gdt_media_view)
        val mLayoutInfo = rootView.findViewById<FrameLayout>(R.id.layout_info)

        mMediaContainer?.layoutParams?.height = ScreenUtil.getDisplayMetricsWidth(container.context) / 3 * 9 / 16

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
        clickableViews.add(mLayoutInfo)
        clickableViews.add(mAdGdtMediaPlayer)
        clickableViews.add(mImgPoster)

        val logoParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
        logoParams.gravity = Gravity.START or Gravity.BOTTOM

        adObject.bindAdToView(container.context, mNativeAdContainer, logoParams, clickableViews)
        adObject.setNativeAdEventListener(object : NativeADEventListener {
            override fun onADStatusChanged() {
                "onADStatusChanged".logd(TAG)
            }

            override fun onADError(error: AdError?) {
                "onADError".logd(TAG)
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
                val videoOption = VideoOption.Builder()
                        .setAutoPlayMuted(true)
                        .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS)
                        .build()
                adObject.bindMediaView(mAdGdtMediaPlayer, videoOption, object : NativeADMediaListener {
                    override fun onVideoInit() {
                        "onVideoInit".logd(TAG)
                    }

                    override fun onVideoStop() {
                        "onVideoStop".logd(TAG)
                    }

                    override fun onVideoPause() {
                        "onVideoPause".logd(TAG)
                    }

                    override fun onVideoStart() {
                        "onVideoStart".logd(TAG)
                    }

                    override fun onVideoError(p0: AdError?) {
                        "onVideoError".logd(TAG)
                    }

                    override fun onVideoCompleted() {
                        "onVideoCompleted".logd(TAG)
                    }

                    override fun onVideoLoading() {
                        "onVideoLoading".logd(TAG)
                    }

                    override fun onVideoReady() {
                        "onVideoReady".logd(TAG)
                    }

                    override fun onVideoLoaded(p0: Int) {
                        "onVideoLoaded".logd(TAG)
                    }

                    override fun onVideoClicked() {
                        "onVideoClicked".logd(TAG)
                    }

                    override fun onVideoResume() {
                        "onVideoResume".logd(TAG)
                    }
                })
                adObject.startVideo()
            }
        }
    }
}