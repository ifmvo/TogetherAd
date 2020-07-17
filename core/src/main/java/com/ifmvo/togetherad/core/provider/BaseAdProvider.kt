package com.ifmvo.togetherad.core.provider

import android.os.Handler
import android.os.Looper
import android.support.annotation.NonNull
import com.ifmvo.togetherad.core.listener.*
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi

/*
 * Created by Matthew Chen on 2020-04-03.
 */
abstract class BaseAdProvider : IAdProvider {

    /**
     * --------------------------- 开屏 ---------------------------
     */
    protected fun callbackSplashStartRequest(@NonNull adProviderType: String, @NonNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackSplashLoaded(@NonNull adProviderType: String, @NonNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackSplashClicked(@NonNull adProviderType: String, @NonNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackSplashExposure(@NonNull adProviderType: String, @NonNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExposure(adProviderType)
        }
    }

    protected fun callbackSplashFailed(@NonNull adProviderType: String, @NonNull listener: SplashListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
        }
    }

    protected fun callbackSplashDismiss(@NonNull adProviderType: String, @NonNull listener: SplashListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 消失了".logi()
            listener.onAdDismissed(adProviderType)
        }
    }

    /**
     * --------------------------- 原生信息流 ---------------------------
     */
    protected fun callbackFlowStartRequest(@NonNull adProviderType: String, @NonNull listener: NativeListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackFlowLoaded(@NonNull adProviderType: String, @NonNull listener: NativeListener, @NonNull adList: List<Any>) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了, 请求到${adList.size}个广告".logi()
            listener.onAdLoaded(adProviderType, adList)
        }
    }

    protected fun callbackFlowFailed(@NonNull adProviderType: String, @NonNull listener: NativeListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
        }
    }

    /**
     * --------------------------- 激励广告 ---------------------------
     */
    protected fun callbackRewardStartRequest(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackRewardLoaded(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackRewardFailed(@NonNull adProviderType: String, @NonNull listener: RewardListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
        }
    }

    protected fun callbackRewardClicked(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    protected fun callbackRewardShow(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 展示了".logi()
            listener.onAdShow(adProviderType)
        }
    }

    protected fun callbackRewardExpose(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackRewardVideoComplete(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 播放完成".logi()
            listener.onAdVideoComplete(adProviderType)
        }
    }

    protected fun callbackRewardVideoCached(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 视频已缓存".logi()
            listener.onAdVideoCached(adProviderType)
        }
    }

    protected fun callbackRewardVerify(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 激励验证".logi()
            listener.onAdRewardVerify(adProviderType)
        }
    }

    protected fun callbackRewardClosed(@NonNull adProviderType: String, @NonNull listener: RewardListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    /**
     * --------------------------- Banner 横幅广告 ---------------------------
     */
    protected fun callbackBannerStartRequest(@NonNull adProviderType: String, @NonNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackBannerLoaded(@NonNull adProviderType: String, @NonNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackBannerFailed(@NonNull adProviderType: String, @NonNull listener: BannerListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
        }
    }

    protected fun callbackBannerClosed(@NonNull adProviderType: String, @NonNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackBannerExpose(@NonNull adProviderType: String, @NonNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackBannerClicked(@NonNull adProviderType: String, @NonNull listener: BannerListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }

    /**
     * --------------------------- Interaction 插屏广告 ---------------------------
     */
    protected fun callbackInterStartRequest(@NonNull adProviderType: String, @NonNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 开始请求".logi()
            listener.onAdStartRequest(adProviderType)
        }
    }

    protected fun callbackInterLoaded(@NonNull adProviderType: String, @NonNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求成功了".logi()
            listener.onAdLoaded(adProviderType)
        }
    }

    protected fun callbackInterFailed(@NonNull adProviderType: String, @NonNull listener: InterListener, failedMsg: String?) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 请求失败了：$failedMsg".loge()
            listener.onAdFailed(adProviderType, failedMsg)
        }
    }

    protected fun callbackInterClosed(@NonNull adProviderType: String, @NonNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 关闭了".logi()
            listener.onAdClose(adProviderType)
        }
    }

    protected fun callbackInterExpose(@NonNull adProviderType: String, @NonNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 曝光了".logi()
            listener.onAdExpose(adProviderType)
        }
    }

    protected fun callbackInterClicked(@NonNull adProviderType: String, @NonNull listener: InterListener) {
        Handler(Looper.getMainLooper()).post {
            "${adProviderType}: 点击了".logi()
            listener.onAdClicked(adProviderType)
        }
    }
}