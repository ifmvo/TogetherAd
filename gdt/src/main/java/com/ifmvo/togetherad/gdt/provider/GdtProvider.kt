package com.ifmvo.togetherad.gdt.provider

import com.qq.e.ads.cfg.DownAPPConfirmPolicy
import com.qq.e.ads.cfg.VideoOption
import com.qq.e.ads.nativ.ADSize


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
        var maxFetchDelay = 4000

    }

    /**
     * --------------------------- 激励 ---------------------------
     */
    object Reward {

        //是否有声播放
        var volumeOn = true

        //如果开启了服务器验证，用来获取此次交易的信息
        var verificationOption: Any? = null

    }

    /**
     * --------------------------- 横幅 ---------------------------
     */
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

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非 wifi 展示），NoConfirm（所有情况不展示）
        var downAPPConfirmPolicy: DownAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //测试性接口，不保证有效。传入 app 内类目信息
        var categories: List<String>? = null

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
        var autoPlayPolicyVideoOption2: Int = VideoOption.AutoPlayPolicy.ALWAYS

        // WIFI 环境下可以自动播放视频 ( 模板1.0 )
        var autoPlayPolicy: Int = VideoOption.AutoPlayPolicy.ALWAYS

        //视频最小时长
        var minVideoDuration: Int = 0

        //视频最大时长
        var maxVideoDuration: Int = 0

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非wifi 展示），NoConfirm（所有情况不展示）
        var downAPPConfirmPolicy: DownAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //广告的宽高
        internal var adWidth = ADSize.FULL_WIDTH
        internal var adHeight = ADSize.AUTO_HEIGHT

        fun setAdSize(widthDp: Int = ADSize.FULL_WIDTH, heightDp: Int = ADSize.AUTO_HEIGHT) {
            adWidth = widthDp
            adHeight = heightDp
        }
    }

    /**
     * --------------------------- 全屏视频 ---------------------------
     */
    object FullVideo {

        // 自动播放时为静音
        var autoPlayMuted = true

        // WIFI 环境下可以自动播放视频
        var autoPlayPolicy: Int = VideoOption.AutoPlayPolicy.ALWAYS

        //视频最小时长
        var minVideoDuration: Int = 0

        //视频最大时长
        var maxVideoDuration: Int = 0

    }
}