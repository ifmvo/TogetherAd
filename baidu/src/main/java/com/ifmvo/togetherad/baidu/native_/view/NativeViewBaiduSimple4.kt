package com.ifmvo.togetherad.baidu.native_.view

import android.os.CountDownTimer
import android.view.ViewGroup
import com.ifmvo.togetherad.baidu.R
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple3
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewBaiduSimple4(onDismiss: (providerType: String) -> Unit) : BaseNativeViewBaidu() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss
    private var mTimer: CountDownTimer? = null

    override fun getLayoutRes(): Int {
        return R.layout.layout_native_view_baidu_simple_4
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        super.showNative(adProviderType, adObject, container, listener)

        getMainImageView()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)

        //添加跳过按钮
        val customSkipView = SplashSkipViewSimple3()
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
}