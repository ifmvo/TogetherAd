package com.ifmvo.togetherad.core.provider

import android.app.Activity
import android.view.ViewGroup
import androidx.annotation.NonNull
import com.ifmvo.togetherad.core.listener.FlowListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
interface IAdProvider {

    fun showSplashAd(

            //由于百度必须使用 Activity，所以这里统一传
            @NonNull activity: Activity,

            //当前广告位的别名
            @NonNull alias: String,

            //开屏广告的容器，开屏广告请求到之后会自动添加进去
            @NonNull container: ViewGroup,

            //回调
            @NonNull listener: SplashListener
    )

    fun getNativeAdList(
            @NonNull activity: Activity,
            @NonNull alias: String,
            maxCount: Int,
            @NonNull listener: FlowListener
    )

//    fun showNativeAd(
//            @NonNull adObject: Any,
//            @NonNull container: ViewGroup,
//            flowTemplate: BaseFlowTemplate
//    )

    fun requestRewardAd(activity: Activity, alias: String, listener: RewardListener)

}