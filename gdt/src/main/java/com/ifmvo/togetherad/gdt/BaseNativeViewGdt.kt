package com.ifmvo.togetherad.gdt

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.NativeADEventListener
import com.qq.e.ads.nativ.NativeADMediaListener
import com.qq.e.ads.nativ.NativeUnifiedADData
import com.qq.e.ads.nativ.widget.NativeAdContainer
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError


/**
 *
 * Created by Matthew Chen on 2020/9/16.
 */
abstract class BaseNativeViewGdt : BaseNativeView() {

    //设置视频广告在预览页自动播放时是否静音，默认为true，静音自动播放；
    // 模板渲染视频、插屏2.0视频、自渲染2.0视频都可使用
    var autoPlayMuted = true

    //设置视频广告在预览页自动播放的网络条件：
    // VideoOption.AutoPlayPolicy.WIFI 表示只在WiFi下自动播放；
    // VideoOption.AutoPlayPolicy.ALWAYS 表示始终自动播放，不区分当前网络；
    // VideoOption.AutoPlayPolicy.NEVER 表示始终都不自动播放，不区分当前网络，但在WiFi时会预下载视频资源；
    // 默认为始终自动播放；模板渲染视频、插屏2.0视频、自渲染2.0视频都可使用
    var autoPlayPolicy: Int = VideoOption.AutoPlayPolicy.ALWAYS

    //设置视频广告在预览页未开始播放时是否显示封面图，默认为true，显示封面图；
    // 只对自渲染2.0视频广告生效
    var needCoverImage = true

    //设置视频广告在在预览页播放过程中是否显示进度条，默认为true，显示进度条；
    // 只对自渲染2.0视频广告生效
    var needProgressBar = true

    //设置是否允许用户在预览页点击视频播放器区域控制视频的暂停或播放，默认为false，用户点击时的表现与点击clickableViews一致；
    // 如果为true，用户点击时将收到NativeADMediaListener.onVideoClicked回调，而不是NativeADEventListener.onADClicked回调，因为此时并不是广告点击。只对自渲染2.0视频广告生效
    var enableUserControl = false

    //用户在预览页点击clickableViews或视频区域(setEnableUserControl设置为false)时是否跳转到详情页，默认为true，跳转到详情页；只对自渲染2.0视频广告生效
    var enableDetailPage = true

    //设置视频详情页是否静音播放，默认值为false，即有声播放；模板渲染视频、插屏2.0视频、自渲染2.0视频都可使用
    var detailPageMuted = false

    internal var rootView: View? = null

    internal open fun getLayoutRes(): Int {
        return R.layout.layout_native_view_gdt
    }

    internal open fun getNativeAdContainer(): NativeAdContainer? {
        return rootView?.findViewById(R.id.native_ad_container)
    }

    //icon 图
    internal open fun getIconImageView(): ImageView? {
        return rootView?.findViewById(R.id.img_logo)
    }

    //action 按钮（ 下载、浏览、打电话。。。 ）
    internal open fun getActionButton(): Button? {
        return rootView?.findViewById(R.id.btn_download)
    }

    //标题文字
    internal open fun getTitleTextView(): TextView? {
        return rootView?.findViewById(R.id.text_title)
    }

    //描述文字
    internal open fun getDescTextView(): TextView? {
        return rootView?.findViewById(R.id.text_desc)
    }

    //视频控件
    internal open fun getMediaView(): MediaView? {
        return rootView?.findViewById(R.id.gdt_media_view)
    }

    //主图
    internal open fun getMainImageView(): ImageView? {
        return rootView?.findViewById(R.id.img_poster)
    }

    //视频是否静音按钮
    internal open fun getMuteCheckBox(): CheckBox? {
        return rootView?.findViewById(R.id.btn_mute)
    }

    //视频播放按钮
    internal open fun getPlayButton(): Button? {
        return rootView?.findViewById(R.id.btn_play)
    }

    //视频暂停按钮
    internal open fun getPauseButton(): Button? {
        return rootView?.findViewById(R.id.btn_pause)
    }

    //视频停止按钮
    internal open fun getStopButton(): Button? {
        return rootView?.findViewById(R.id.btn_stop)
    }

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        if (adObject !is NativeUnifiedADData) {
            return
        }

        //findView
        rootView = View.inflate(container.context, getLayoutRes(), container)

        //Image
        getIconImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.iconUrl) }

        //标题和描述
        getTitleTextView()?.text = adObject.title
        getDescTextView()?.text = adObject.desc

        //图片还是视频
        when (adObject.adPatternType) {
            AdPatternType.NATIVE_1IMAGE_2TEXT, AdPatternType.NATIVE_2IMAGE_2TEXT, AdPatternType.NATIVE_3IMAGE -> {
                getMediaView()?.visibility = View.GONE
                getMainImageView()?.visibility = View.VISIBLE
                getMainImageView()?.let { TogetherAd.mImageLoader?.loadImage(container.context, it, adObject.imgUrl) }
            }
            AdPatternType.NATIVE_VIDEO -> {
                getMediaView()?.visibility = View.VISIBLE
                getMainImageView()?.visibility = View.GONE
            }
        }

        //设置可点的View
        val clickableViews = mutableListOf<View>()
        val customClickableViews = mutableListOf<View>()
        getActionButton()?.let { customClickableViews.add(it) }
        getMainImageView()?.let { clickableViews.add(it) }
        getIconImageView()?.let { clickableViews.add(it) }
        adObject.bindAdToView(container.context, getNativeAdContainer(), null, clickableViews, customClickableViews)

        //视频需要设置 静音、播放、暂停、停止按钮
        if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {

            val videoOption = VideoOption.Builder()
                    .setAutoPlayPolicy(autoPlayPolicy)
                    .setAutoPlayMuted(autoPlayMuted)
                    .setDetailPageMuted(detailPageMuted)
                    .setNeedCoverImage(needCoverImage)
                    .setNeedProgressBar(needProgressBar)
                    .setEnableDetailPage(enableDetailPage)
                    .setEnableUserControl(enableUserControl)
                    .build()

            adObject.bindMediaView(getMediaView(), videoOption, object : NativeADMediaListener {
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
            getMuteCheckBox()?.visibility = View.VISIBLE
            getPlayButton()?.visibility = View.VISIBLE
            getPauseButton()?.visibility = View.VISIBLE
            getStopButton()?.visibility = View.VISIBLE

            getMuteCheckBox()?.isChecked = autoPlayMuted
            getMuteCheckBox()?.setOnCheckedChangeListener { _, isChecked ->
                adObject.setVideoMute(isChecked)
            }

            getPlayButton()?.setOnClickListener {
                adObject.startVideo()
            }

            getPauseButton()?.setOnClickListener {
                adObject.pauseVideo()
            }

            getStopButton()?.setOnClickListener {
                adObject.stopVideo()
            }
        } else {
            getMuteCheckBox()?.visibility = View.GONE
            getPlayButton()?.visibility = View.GONE
            getPauseButton()?.visibility = View.GONE
            getStopButton()?.visibility = View.GONE
        }

        //广告的事件
        adObject.setNativeAdEventListener(object : NativeADEventListener {
            override fun onADStatusChanged() {
                //根据广告的状态改变按钮的文字
                getActionButton()?.let { it.text = getActionBtnText(adObject) }
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

        //设置按钮的初始文字
        getActionButton()?.let { it.text = getActionBtnText(adObject) }
    }

    /**
     * 根据广告的状态返回按钮应该展示的文字
     */
    internal open fun getActionBtnText(ad: NativeUnifiedADData): String {
        if (!ad.isAppAd) {
            return "浏览"
        }
        return when (ad.appStatus) {
            0 -> "下载"
            1 -> "启动"
            2 -> "更新"
            4 -> ad.progress.toString() + "%"
            8 -> "安装"
            16 -> "下载失败，重新下载"
            else -> "浏览"
        }
    }
}