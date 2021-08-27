package com.ifmvo.togetherad.csj.provider

import com.bytedance.sdk.openadsdk.TTAdConstant
import com.ifmvo.togetherad.core.custom.splashSkip.BaseSplashSkipView

/**
 * 广告提供商：穿山甲
 *
 * Created by Matthew Chen on 2020-04-03.
 */
open class CsjProvider : CsjProviderSplash() {

    /**
     * --------------------------- 开屏 ---------------------------
     */
    object Splash {

        //是否模板类型广告请求
        var isExpress = false

        //超时时间
        var maxFetchDelay = 4000

        //自定义按钮
        var customSkipView: BaseSplashSkipView? = null

        //图片的宽高
        internal var imageAcceptedSizeWidth = 1080

        internal var imageAcceptedSizeHeight = 1920

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }

        var splashButtonType = TTAdConstant.SPLASH_BUTTON_TYPE_FULL_SCREEN
    }

    /**
     * --------------------------- 横幅Banner ---------------------------
     */
    object Banner {

        var supportDeepLink: Boolean = true

        //模板的宽高
        internal var expressViewAcceptedSizeWidth = -1f

        internal var expressViewAcceptedSizeHeight = -1f

        fun setExpressViewAcceptedSize(widthDp: Float, heightDp: Float) {
            expressViewAcceptedSizeWidth = widthDp
            expressViewAcceptedSizeHeight = heightDp
        }

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    /**
     * --------------------------- 插屏 ---------------------------
     */
    object Inter {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var expressViewAcceptedSizeWidth = -1f

        internal var expressViewAcceptedSizeHeight = -1f

        /**
         * 说明：插屏在请求过程中，需要按照所选比例的实际尺寸请求，比如 1:1 的比例可以请求 600*600，2:3 的比例可以请求 600*900
         */
        fun setExpressViewAcceptedSize(width: Float, height: Float) {
            expressViewAcceptedSizeWidth = width
            expressViewAcceptedSizeHeight = height
        }
    }

    /**
     * --------------------------- 原生自渲染 ---------------------------
     */
    object Native {
        //如果需要使用穿山甲的原生广告，必须在请求之前设置类型。
        var nativeAdType = -1

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 1080

        internal var imageAcceptedSizeHeight = 1080 * 9 / 16

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }
    }

    /**
     * --------------------------- 原生自渲染模板 ---------------------------
     */
    object NativeExpress {

        var supportDeepLink: Boolean = true

        //期望模板广告view的size,单位dp
        internal var expressViewAcceptedSizeWidth = 0f

        //高度设置为0, 则高度会自适应
        internal var expressViewAcceptedSizeHeight = 0f

        /**
         * 期望模板广告view的size, 单位dp
         */
        fun setExpressViewAcceptedSize(widthDp: Float, heightDp: Float) {
            expressViewAcceptedSizeWidth = widthDp
            expressViewAcceptedSizeHeight = heightDp
        }
    }

    /**
     * --------------------------- 激励 ---------------------------
     */
    object Reward {

        //是否模板类型广告请求
        var isExpress = true

        //表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
        var userID: String? = null

        var supportDeepLink: Boolean = true

        //奖励的名称
        var rewardName: String? = null

        //奖励的数量
        var rewardAmount: Int = -1

        //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
        var orientation: Int = TTAdConstant.VERTICAL

        //用户透传的信息，可不传
        var mediaExtra: String? = null

        //设置是否在视频播放页面展示下载bar
        var showDownLoadBar: Boolean = true

        /**
         * onAdRewardVerify 回调中用于判断校验结果（ 后台校验中用到 ）
         */
        var rewardVerify: Boolean = false
            internal set

        var errorCode: Int = 0
            internal set

        var errorMsg: String? = null
            internal set
    }

    /**
     * --------------------------- 全屏视频 ---------------------------
     */
    object FullVideo {

        //是否模板类型广告请求
        var isExpress = true

        var supportDeepLink: Boolean = true

        //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
        var orientation: Int = TTAdConstant.VERTICAL

        var ritScenes: TTAdConstant.RitScenes? = null

    }
}
