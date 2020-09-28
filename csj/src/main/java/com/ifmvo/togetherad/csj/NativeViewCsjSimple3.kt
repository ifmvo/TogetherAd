package com.ifmvo.togetherad.csj

import android.os.CountDownTimer
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple2
import com.ifmvo.togetherad.core.listener.NativeViewListener
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple3(onDismiss: (providerType: String) -> Unit) : BaseNativeViewCsj() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_csj_simple_3
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