package com.ifmvo.togetherad.core.provider

import android.app.Activity
import android.support.annotation.NonNull
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface IAdProvider {

    fun showSplashAd(

            //由于百度必须使用 Activity，所以这里统一传
            @NonNull activity: Activity,

            @NonNull adProviderType: String,

            //当前广告位的别名
            @NonNull alias: String,

            //开屏广告的容器，开屏广告请求到之后会自动添加进去
            @NonNull container: ViewGroup,

            //回调
            @NonNull listener: SplashListener
    )

    fun showBannerAd(

            @NonNull activity: Activity,

            @NonNull adProviderType: String,

            @NonNull alias: String,

            @NonNull container: ViewGroup,

            @NonNull listener: BannerListener
    )

    fun destroyBannerAd()

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
    fun isBelongTheProvider(

            @NonNull adObject: Any

    ): Boolean

    fun requestRewardAd(
            @NonNull activity: Activity,
            @NonNull adProviderType: String,
            @NonNull alias: String,
            @NonNull listener: RewardListener
    )

    fun showRewardAd(
            @NonNull activity: Activity
    )

}