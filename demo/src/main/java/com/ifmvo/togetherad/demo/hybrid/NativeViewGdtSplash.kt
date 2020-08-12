package com.ifmvo.togetherad.demo.hybrid

import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.demo.R
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.NativeADEventListener
import com.qq.e.ads.nativ.NativeADMediaListener
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtSplash(onDismiss: (providerType: String) -> Unit) : BaseNativeView() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    private val TAG = "NativeViewGdtSplash"

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {

        if (adObject !is NativeUnifiedADData) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_gdt_splash, container)

        val mNativeAdContainer = rootView.findViewById<NativeAdContainer>(R.id.native_ad_container)
        val mSuperParent = rootView.findViewById<View>(R.id.super_parent)
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
        clickableViews.add(mSuperParent)
        clickableViews.add(mImgPoster)
        clickableViews.add(mAdGdtMediaPlayer)
        adObject.bindAdToView(container.context, mNativeAdContainer, null, clickableViews)
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
                val videoOption = VideoOption.Builder().setAutoPlayMuted(true).setAutoPlayPolicy(VideoOption.AutoPlayPolicy.ALWAYS).build()
                adObject.bindMediaView(mAdGdtMediaPlayer, videoOption, object : NativeADMediaListener {
                    override fun onVideoInit() {"onVideoInit".logd(TAG)}
                    override fun onVideoStop() {"onVideoStop".logd(TAG)}
                    override fun onVideoPause() {"onVideoPause".logd(TAG)}
                    override fun onVideoStart() {"onVideoStart".logd(TAG)}
                    override fun onVideoError(p0: AdError?) {"onVideoError".logd(TAG)}
                    override fun onVideoCompleted() {"onVideoCompleted".logd(TAG)}
                    override fun onVideoLoading() {"onVideoLoading".logd(TAG)}
                    override fun onVideoReady() {"onVideoReady".logd(TAG)}
                    override fun onVideoLoaded(p0: Int) {"onVideoLoaded".logd(TAG)}
                    override fun onVideoClicked() {"onVideoClicked".logd(TAG)}
                    override fun onVideoResume() {"onVideoResume".logd(TAG)}
                })
                adObject.startVideo()
            }
        }

        //添加跳过按钮
        val customSkipView = AdHelperSplashHybrid.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)
        skipView?.run {
            container.addView(this, customSkipView.getLayoutParams())
            setOnClickListener {
                mTimer?.cancel()
                mOnDismiss.invoke(adProviderType)
            }
        }

        //开始倒计时
        mTimer?.cancel()
        mTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                mOnDismiss.invoke(adProviderType)
            }

            override fun onTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000f).roundToInt()
                customSkipView?.handleTime(second)
            }
        }
        mTimer?.start()
    }

    private var mTimer: CountDownTimer? = null
}