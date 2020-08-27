package com.ifmvo.togetherad.core.provider

import android.os.Handler
import android.os.Looper
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import org.jetbrains.annotations.NotNull

/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseAdProvider : IAdProvider {

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

    protected fun callbackSplashFailed(@NotNull adProviderType: String, @NotNull listener: SplashListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
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
    protected fun callbackFlowStartRequest(@NotNull adProviderType: String, @NotNull listener: NativeListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackFlowLoaded(@NotNull adProviderType: String, @NotNull listener: NativeListener, @NotNull adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
        }
    }

    protected fun callbackFlowFailed(@NotNull adProviderType: String, @NotNull listener: NativeListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
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

    protected fun callbackRewardFailed(@NotNull adProviderType: String, @NotNull listener: RewardListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
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

    protected fun callbackBannerFailed(@NotNull adProviderType: String, @NotNull listener: BannerListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
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

    protected fun callbackInterFailed(@NotNull adProviderType: String, @NotNull listener: InterListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
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