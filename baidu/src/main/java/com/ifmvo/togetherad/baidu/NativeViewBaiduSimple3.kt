package com.ifmvo.togetherad.baidu

import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.mobad.feeds.NativeResponse
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple2
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewBaiduSimple3(onDismiss: (providerType: String) -> Unit) : BaseNativeView() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is NativeResponse) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_baidu_simple_3, container)

        val mImgPoster = rootView.findViewById<ImageView>(R.id.img_poster)
        val mLlParent = rootView.findViewById<LinearLayout>(R.id.ll_parent)
        val mTvTitle = rootView.findViewById<TextView>(R.id.tv_title)
        val mTvDesc = rootView.findViewById<TextView>(R.id.tv_desc)
        val mImgLogoBaidu = rootView.findViewById<ImageView>(R.id.img_logo_baidu)
        val mImgAd = rootView.findViewById<ImageView>(R.id.img_ad)

        mTvTitle?.text = adObject.title
        mTvDesc?.text = adObject.desc

        TogetherAd.mImageLoader?.loadImage(container.context, mImgLogoBaidu, adObject.baiduLogoUrl)
        TogetherAd.mImageLoader?.loadImage(container.context, mImgAd, adObject.adLogoUrl)

        TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster, adObject.imageUrl)
        listener?.onAdExposed(adProviderType)
        adObject.recordImpression(mLlParent)
        mLlParent?.setOnClickListener {
            adObject.handleClick(it)

            listener?.onAdClicked(adProviderType)
        }

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