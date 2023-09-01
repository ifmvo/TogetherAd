package com.ifmvo.togetherad.core.provider

import android.os.Handler
import android.os.Looper
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseAdProvider : IAdProvider {

    protected val tag: String? = this.javaClass.simpleName

    /**
     * --------------------------- 开屏 ---------------------------
     */
    protected fun callbackSplashStartRequest(adProviderType: String, alias: String, listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackSplashLoaded(adProviderType: String, alias: String, listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackSplashClicked(adProviderType: String, listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackSplashExposure(adProviderType: String, listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExposure(adProviderType)
        }
    }

    protected fun callbackSplashFailed(adProviderType: String, alias: String, listener: SplashListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackSplashDismiss(adProviderType: String, listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 消失了".logi()
            listener.onAdDismissed(adProviderType)
        }
    }

    /**
     * --------------------------- 原生信息流 ---------------------------
     */
    protected fun callbackNativeStartRequest(adProviderType: String, alias: String, listener: NativeListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackNativeLoaded(adProviderType: String, alias: String, listener: NativeListener, adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackNativeFailed(adProviderType: String, alias: String, listener: NativeListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    /**
     * --------------------------- 原生信息流模板 ---------------------------
     */
    protected fun callbackNativeExpressStartRequest(adProviderType: String, alias: String, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackNativeExpressLoaded(adProviderType: String, alias: String, listener: NativeExpressListener, adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackNativeExpressFailed(adProviderType: String, alias: String, listener: NativeExpressListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackNativeExpressClicked(adProviderType: String, adObject: Any?, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressShow(adProviderType: String, adObject: Any?, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressRenderSuccess(adProviderType: String, adObject: Any?, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 渲染成功".logi()
            listener.onAdRenderSuccess(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressRenderFail(adProviderType: String, adObject: Any?, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 渲染失败了".loge()
            listener.onAdRenderFail(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressClosed(adProviderType: String, adObject: Any?, listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClosed(adProviderType, adObject)
        }
    }

    /**
     * --------------------------- 激励广告 ---------------------------
     */
    protected fun callbackRewardStartRequest(adProviderType: String, alias: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackRewardLoaded(adProviderType: String, alias: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackRewardFailed(adProviderType: String, alias: String, listener: RewardListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackRewardClicked(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackRewardShow(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType)
        }
    }

    protected fun callbackRewardExpose(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackRewardVideoComplete(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 播放完成".logi()
            listener.onAdVideoComplete(adProviderType)
        }
    }

    protected fun callbackRewardVideoCached(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频已缓存".logi()
            listener.onAdVideoCached(adProviderType)
        }
    }

    protected fun callbackRewardVerify(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 激励验证".logi()
            listener.onAdRewardVerify(adProviderType)
        }
    }

    protected fun callbackRewardClosed(adProviderType: String, listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    /**
     * --------------------------- 全屏视频广告 ---------------------------
     */
    protected fun callbackFullVideoStartRequest(adProviderType: String, alias: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackFullVideoLoaded(adProviderType: String, alias: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackFullVideoFailed(adProviderType: String, alias: String, listener: FullVideoListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackFullVideoClicked(adProviderType: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackFullVideoShow(adProviderType: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType)
        }
    }

    protected fun callbackFullVideoCached(adProviderType: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频已缓存".logi()
            listener.onAdVideoCached(adProviderType)
        }
    }

    protected fun callbackFullVideoClosed(adProviderType: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackFullVideoComplete(adProviderType: String, listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频播放完成了".logi()
            listener.onAdVideoComplete(adProviderType)
        }
    }

    /**
     * --------------------------- Banner 横幅广告 ---------------------------
     */
    protected fun callbackBannerStartRequest(adProviderType: String, alias: String, listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackBannerLoaded(adProviderType: String, alias: String, listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackBannerFailed(adProviderType: String, alias: String, listener: BannerListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackBannerClosed(adProviderType: String, listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackBannerExpose(adProviderType: String, listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackBannerClicked(adProviderType: String, listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    /**
     * --------------------------- Interaction 插屏广告 ---------------------------
     */
    protected fun callbackInterStartRequest(adProviderType: String, alias: String, listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
            TogetherAd.allAdListener?.onAdStartRequest(adProviderType, alias)
        }
    }

    protected fun callbackInterLoaded(adProviderType: String, alias: String, listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
            TogetherAd.allAdListener?.onAdLoaded(adProviderType, alias)
        }
    }

    protected fun callbackInterFailed(adProviderType: String, alias: String, listener: InterListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
            TogetherAd.allAdListener?.onAdFailed(adProviderType, alias, errorMsg)
        }
    }

    protected fun callbackInterClosed(adProviderType: String, listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackInterExpose(adProviderType: String, listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackInterClicked(adProviderType: String, listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }
}