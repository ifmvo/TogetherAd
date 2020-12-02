package com.ifmvo.togetherad.core.provider

import android.os.Handler
import android.os.Looper
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
    protected fun callbackSplashStartRequest(@NotNull adProviderType: String, @NotNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackSplashLoaded(@NotNull adProviderType: String, @NotNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackSplashClicked(@NotNull adProviderType: String, @NotNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackSplashExposure(@NotNull adProviderType: String, @NotNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExposure(adProviderType)
        }
    }

    protected fun callbackSplashFailed(@NotNull adProviderType: String, @NotNull listener: SplashListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackSplashDismiss(@NotNull adProviderType: String, @NotNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 消失了".logi()
            listener.onAdDismissed(adProviderType)
        }
    }

    /**
     * --------------------------- 原生信息流 ---------------------------
     */
    protected fun callbackNativeStartRequest(@NotNull adProviderType: String, @NotNull listener: NativeListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackNativeLoaded(@NotNull adProviderType: String, @NotNull listener: NativeListener, @NotNull adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
        }
    }

    protected fun callbackNativeFailed(@NotNull adProviderType: String, @NotNull listener: NativeListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    /**
     * --------------------------- 原生信息流模板 ---------------------------
     */
    protected fun callbackNativeExpressStartRequest(@NotNull adProviderType: String, @NotNull listener: NativeExpress2Listener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackNativeExpressLoaded(@NotNull adProviderType: String, @NotNull listener: NativeExpress2Listener, @NotNull adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
        }
    }

    protected fun callbackNativeExpressFailed(@NotNull adProviderType: String, @NotNull listener: NativeExpress2Listener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackNativeExpressClicked(@NotNull adProviderType: String, @Nullable adObject: Any?, @NotNull listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressShow(@NotNull adProviderType: String, @Nullable adObject: Any?, @NotNull listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressRenderSuccess(@NotNull adProviderType: String, @Nullable adObject: Any?, @NotNull listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 渲染成功".logi()
            listener.onAdRenderSuccess(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressRenderFail(@NotNull adProviderType: String, @Nullable adObject: Any?, @NotNull listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 渲染失败了".loge()
            listener.onAdRenderFail(adProviderType, adObject)
        }
    }

    protected fun callbackNativeExpressClosed(@NotNull adProviderType: String, @Nullable adObject: Any?, @NotNull listener: NativeExpressListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClosed(adProviderType, adObject)
        }
    }

    /**
     * --------------------------- 激励广告 ---------------------------
     */
    protected fun callbackRewardStartRequest(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackRewardLoaded(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackRewardFailed(@NotNull adProviderType: String, @NotNull listener: RewardListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackRewardClicked(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackRewardShow(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType)
        }
    }

    protected fun callbackRewardExpose(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackRewardVideoComplete(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 播放完成".logi()
            listener.onAdVideoComplete(adProviderType)
        }
    }

    protected fun callbackRewardVideoCached(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频已缓存".logi()
            listener.onAdVideoCached(adProviderType)
        }
    }

    protected fun callbackRewardVerify(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 激励验证".logi()
            listener.onAdRewardVerify(adProviderType)
        }
    }

    protected fun callbackRewardClosed(@NotNull adProviderType: String, @NotNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    /**
     * --------------------------- 全屏视频广告 ---------------------------
     */
    protected fun callbackFullVideoStartRequest(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackFullVideoLoaded(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackFullVideoFailed(@NotNull adProviderType: String, @NotNull listener: FullVideoListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackFullVideoClicked(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackFullVideoShow(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType)
        }
    }

    protected fun callbackFullVideoComplete(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 播放完成".logi()
            listener.onAdVideoComplete(adProviderType)
        }
    }

    protected fun callbackFullVideoCached(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频已缓存".logi()
            listener.onAdVideoCached(adProviderType)
        }
    }

    protected fun callbackFullVideoClosed(@NotNull adProviderType: String, @NotNull listener: FullVideoListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    /**
     * --------------------------- Banner 横幅广告 ---------------------------
     */
    protected fun callbackBannerStartRequest(@NotNull adProviderType: String, @NotNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackBannerLoaded(@NotNull adProviderType: String, @NotNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackBannerFailed(@NotNull adProviderType: String, @NotNull listener: BannerListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackBannerClosed(@NotNull adProviderType: String, @NotNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackBannerExpose(@NotNull adProviderType: String, @NotNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackBannerClicked(@NotNull adProviderType: String, @NotNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    /**
     * --------------------------- Interaction 插屏广告 ---------------------------
     */
    protected fun callbackInterStartRequest(@NotNull adProviderType: String, @NotNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackInterLoaded(@NotNull adProviderType: String, @NotNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackInterFailed(@NotNull adProviderType: String, @NotNull listener: InterListener, errorCode: Int?, errorMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$errorCode $errorMsg".loge()
            listener.onAdFailed(adProviderType, errorMsg)
        }
    }

    protected fun callbackInterClosed(@NotNull adProviderType: String, @NotNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackInterExpose(@NotNull adProviderType: String, @NotNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackInterClicked(@NotNull adProviderType: String, @NotNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }
}