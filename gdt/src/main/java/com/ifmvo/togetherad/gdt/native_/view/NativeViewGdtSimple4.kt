package com.ifmvo.togetherad.gdt.native_.view

import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple3
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.gdt.R
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.widget.NativeAdContainer
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewGdtSimple4(onClose: (providerType: String) -> Unit = {}) : BaseNativeViewGdt() {

    private var mOnClose: (providerType: String) -> Unit = onClose

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_gdt_simple_4
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

        //添加跳过按钮
        val customSkipView = SplashSkipViewSimple3()
        val skipView = customSkipView.onCreateSkipView(container.context)
        skipView.run {
            container.addView(this, customSkipView.getLayoutParams())
            setOnClickListener {
                mTimer?.cancel()
                container.removeAllViews()
                mOnClose.invoke(adProviderType)
            }
        }

        //开始倒计时
        mTimer?.cancel()
        mTimer = object : CountDownTimer(5000, 1000) {
            override fun onFinish() {
                container.removeAllViews()
                mOnClose.invoke(adProviderType)
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