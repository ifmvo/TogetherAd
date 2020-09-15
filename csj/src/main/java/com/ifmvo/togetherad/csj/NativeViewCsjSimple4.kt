package com.ifmvo.togetherad.csj

import android.os.CountDownTimer
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple3
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple4(onDismiss: (providerType: String) -> Unit) : BaseNativeView() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is TTNativeAd) {
            return
        }
        val rootView = View.inflate(container.context, R.layout.layout_native_view_csj_simple_4, container)

        val mSuperParent = rootView.findViewById<ViewGroup>(R.id.super_parent)
        val mFlParent = rootView.findViewById<FrameLayout>(R.id.fl_parent)

        //图片类型的
        val mLlAdContainer = rootView.findViewById<LinearLayout>(R.id.ll_ad_container)
        val mImgPoster0 = rootView.findViewById<ImageView>(R.id.img_poster1)
        val mImgPoster1 = rootView.findViewById<ImageView>(R.id.img_poster2)
        val mImgPoster2 = rootView.findViewById<ImageView>(R.id.img_poster3)
        //视频类型的
        val mFlVideoContainer = rootView.findViewById<FrameLayout>(R.id.fl_ad_container)

        //标题和描述
        val mTvTitle = rootView.findViewById<TextView>(R.id.tv_title)
        val mTvDesc = rootView.findViewById<TextView>(R.id.tv_desc)

        val layoutParams = mFlParent?.layoutParams
        layoutParams?.height = ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16

        // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
        val clickViewList = mutableListOf<View>()
        clickViewList.add(mSuperParent)
        // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
        //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
        val creativeViewList = mutableListOf<View>()
        creativeViewList.add(mSuperParent)
        // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
        adObject.registerViewForInteraction(mSuperParent, clickViewList, creativeViewList, object : TTNativeAd.AdInteractionListener {
            override fun onAdClicked(view: View, ad: TTNativeAd) {
                // 点击普通区域的回调
            }

            override fun onAdCreativeClick(view: View, ad: TTNativeAd) {
                // 点击创意区域的回调
                listener?.onAdClicked(adProviderType)
            }

            override fun onAdShow(ad: TTNativeAd) {
                // 广告曝光展示的回调
                listener?.onAdExposed(adProviderType)
            }
        })

        mTvTitle.text = adObject.title
        mTvDesc.text = adObject.description
        when (adObject.imageMode) {
            //视频类型
            TTAdConstant.IMAGE_MODE_VIDEO, TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL -> {
                mLlAdContainer.visibility = View.VISIBLE
                mFlVideoContainer.visibility = View.GONE
                mImgPoster1.visibility = View.GONE
                mImgPoster2.visibility = View.GONE
                val videoCoverImage = adObject.videoCoverImage

                //信息流如果是视频的话，就只展示视频封面
                if (videoCoverImage != null && videoCoverImage.imageUrl != null) {
                    TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster0, videoCoverImage.imageUrl)
                }
            }
            //单个图片的类型
            TTAdConstant.IMAGE_MODE_LARGE_IMG, TTAdConstant.IMAGE_MODE_SMALL_IMG, TTAdConstant.IMAGE_MODE_VERTICAL_IMG -> {
                mLlAdContainer.visibility = View.VISIBLE
                mFlVideoContainer.visibility = View.GONE
                mImgPoster1.visibility = View.GONE
                mImgPoster2.visibility = View.GONE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster0, imageList[0].imageUrl)
                }
            }
            //多个图片的类型
            TTAdConstant.IMAGE_MODE_GROUP_IMG -> {
                mLlAdContainer.visibility = View.VISIBLE
                mFlVideoContainer.visibility = View.GONE
                mImgPoster1.visibility = View.VISIBLE
                mImgPoster2.visibility = View.VISIBLE
                val imageList = adObject.imageList

                if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                    TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster0, imageList[0].imageUrl)
                }
                if (imageList.isNotEmpty() && imageList.size > 1 && imageList[1] != null && imageList[1].isValid) {
                    TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster1, imageList[1].imageUrl)
                }
                if (imageList.isNotEmpty() && imageList.size > 2 && imageList[2].isValid) {
                    TogetherAd.mImageLoader?.loadImage(container.context, mImgPoster2, imageList[2].imageUrl)
                }
            }
        }

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

    private var mTimer: CountDownTimer? = null
}