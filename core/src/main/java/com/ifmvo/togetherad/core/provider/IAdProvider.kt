package com.ifmvo.togetherad.core.provider

import android.app.Activity
import android.support.annotation.NonNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.*

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface IAdProvider {

    /**
     * 开屏广告
     */
    fun showSplashAd(
            @NonNull activity: Activity,//由于百度必须使用 Activity，所以这里统一传
            @NonNull adProviderType: String,
            @NonNull alias: String,//当前广告位的别名
            @NonNull container: ViewGroup,//开屏广告的容器，开屏广告请求到之后会自动添加进去
            @NonNull listener: SplashListener//回调
    )

    /**
     * Banner 广告
     */
    fun showBannerAd(
            @NonNull activity: Activity,
            @NonNull adProviderType: String,
            @NonNull alias: String,
            @NonNull container: ViewGroup,
            @NonNull listener: BannerListener
    )

    //销毁 Banner 广告
    fun destroyBannerAd()

    /**
     * 插屏
     */
    fun requestInterAd(
            @NonNull activity: Activity,
            @NonNull adProviderType: String,
            @NonNull alias: String,
            @NonNull listener: InterListener
    )

    //展示插屏广告
    fun showInterAd(
            @NonNull activity: Activity
    )

    //销毁插屏广告
    fun destroyInterAd()

    /**
     * 获取自渲染信息流
     */
    fun getNativeAdList(
            @NonNull activity: Activity,
            @NonNull adProviderType: String,
            @NonNull alias: String,
            maxCount: Int,
            @NonNull listener: NativeListener
    )

    /**
     * 判断原生广告对象是否属于这个提供商
     */
    fun nativeAdIsBelongTheProvider(
            @NonNull adObject: Any
    ): Boolean

    //控制原生自渲染的生命周期
    fun resumeNativeAd(
            @NonNull adObject: Any
    )

    //控制原生自渲染的生命周期
    fun pauseNativeAd(
        @NonNull adObject: Any
    )

    //控制原生自渲染的生命周期
    fun destroyNativeAd(
            @NonNull adObject: Any
    )

    /**
     * 请求激励广告
     */
    fun requestRewardAd(
            @NonNull activity: Activity,
            @NonNull adProviderType: String,
            @NonNull alias: String,
            @NonNull listener: RewardListener
    )

    /**
     * 展示激励广告
     */
    fun showRewardAd(
            @NonNull activity: Activity
    )

}