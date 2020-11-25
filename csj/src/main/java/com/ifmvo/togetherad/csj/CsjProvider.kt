package com.ifmvo.togetherad.csj

import com.bytedance.sdk.openadsdk.TTAdConstant
import com.ifmvo.togetherad.csj.provider.CsjProviderSplash

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

        //超时时间
        var maxFetchDelay = 3000

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 1080

        internal var imageAcceptedSizeHeight = 1920

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }
    }

    /**
     * --------------------------- 横幅Banner ---------------------------
     */
    object Banner {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var expressViewAcceptedSizeWidth = -1f

        internal var expressViewAcceptedSizeHeight = -1f

        fun setExpressViewAcceptedSize(width: Float, height: Float) {
            expressViewAcceptedSizeWidth = width
            expressViewAcceptedSizeHeight = height
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
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 600

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
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
     * --------------------------- 激励 ---------------------------
     */
    object Reward {
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

}
