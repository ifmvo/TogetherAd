package com.ifmvo.togetherad.gdt.provider

import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView
import com.qq.e.ads.cfg.BrowserType
import com.qq.e.ads.cfg.DownAPPConfirmPolicy
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize
import com.qq.e.ads.nativ.express2.VideoOption2


/**
 * 广告提供商：优量汇（广点通）
 *
 * Created by Matthew Chen on 2020-04-03.
 */
open class GdtProvider : GdtProviderSplash() {

    /**
     * --------------------------- 开屏 ---------------------------
     */
    object Splash {

        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以给 fetchDelay 设为0。
         */
        var maxFetchDelay = 0

        //自定义按钮
        var customSkipView: BaseSplashSkipView? = null

    }

    object Banner {

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    /**
     * --------------------------- 原生自渲染 ---------------------------
     */
    object Native {
        //设置返回视频广告的最大视频时长（闭区间，可单独设置），单位:秒，合法输入为：5<=maxVideoDuration<=60. 此设置会影响广告填充，请谨慎设置
        var maxVideoDuration = 60

        //设置返回视频广告的最小视频时长（闭区间，可单独设置），单位:秒 此设置会影响广告填充，请谨慎设置
        var minVideoDuration = 5

        //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认，SDK 按照默认逻辑选择)
        var browserType: BrowserType = BrowserType.Default

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非 wifi 展示），NoConfirm（所有情况不展示）
        var downAPPConfirmPolicy: DownAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //测试性接口，不保证有效。传入 app 内类目信息
        var categories: List<String>? = null

        //设置本次拉取的视频广告，从用户角度看到的视频播放策略；
        // 可选项包括自VideoOption.VideoPlayPolicy.AUTO(在用户看来，视频广告是自动播放的)和VideoOption.VideoPlayPolicy.MANUAL(在用户看来，视频广告是手动播放的)；
        // 如果广告位支持视频，强烈建议调用此接口设置视频广告的播放策略，有助于提高eCPM值；如果广告位不支持视频，忽略本接口
        var videoPlayPolicy: Int = VideoOption.VideoPlayPolicy.AUTO
    }

    /**
     * --------------------------- 原生模板 ---------------------------
     */
    object NativeExpress {

        // 自动播放时为静音
        var autoPlayMuted = true

        //视频详情页播放时默认不静音
        var detailPageMuted = false

        // WIFI 环境下可以自动播放视频( 模板2.0 )
        var autoPlayPolicyVideoOption2: VideoOption2.AutoPlayPolicy = VideoOption2.AutoPlayPolicy.ALWAYS

        // WIFI 环境下可以自动播放视频 ( 模板1.0 )
        var autoPlayPolicy: Int = VideoOption.AutoPlayPolicy.ALWAYS

        /**
         * 如果广告位支持视频广告，强烈建议在调用loadData请求广告前调用setVideoPlayPolicy，有助于提高视频广告的eCPM值 <br/>
         * 如果广告位仅支持图文广告，则无需调用
         * 设置本次拉取的视频广告，从用户角度看到的视频播放策略<p/>
         * "用户角度"特指用户看到的情况，并非SDK是否自动播放，与自动播放策略AutoPlayPolicy的取值并非一一对应 <br/>
         * 如自动播放策略为AutoPlayPolicy.WIFI，但此时用户网络为4G环境，在用户看来就是手工播放的
         */
        var videoPlayPolicy = VideoOption.VideoPlayPolicy.AUTO

        //视频最小时长
        var minVideoDuration: Int = 0

        //视频最大时长
        var maxVideoDuration: Int = 0

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非wifi 展示），NoConfirm（所有情况不展示）
        var downAPPConfirmPolicy: DownAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认），SDK 按照默认逻辑选择
        var browserType: BrowserType = BrowserType.Default

        //广告的宽高
        internal var adWidth = ADSize.FULL_WIDTH
        internal var adHeight = ADSize.AUTO_HEIGHT

        fun setAdSize(widthDp: Int = ADSize.FULL_WIDTH, heightDp: Int = ADSize.AUTO_HEIGHT) {
            adWidth = widthDp
            adHeight = heightDp
        }
    }
}