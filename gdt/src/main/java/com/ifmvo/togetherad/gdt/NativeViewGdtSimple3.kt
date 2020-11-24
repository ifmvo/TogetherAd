package com.ifmvo.togetherad.gdt

import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple1
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple2
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
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtSimple3(onDismiss: (providerType: String) -> Unit) : BaseNativeViewGdt() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_gdt_simple_3
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

        //添加跳过按钮
        val customSkipView = SplashSkipViewSimple2()
        val skipView = customSkipView.onCreateSkipView(container.context)
        skipView.run {
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
                customSkipView.handleTime(second)
            }
        }
        mTimer?.start()

    }

    private var mTimer: CountDownTimer? = null
}