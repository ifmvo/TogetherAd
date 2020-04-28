package com.ifmvo.togetherad.mango

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.mangolm.ad.reward.MGMobReward
import com.mangolm.ad.reward.MGMobRewardListener

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-27.
 */
class MangoProvider : BaseAdProvider() {

    val adProviderType = AdProviderType.MANGO

    override fun showSplashAd(
        activity: Activity,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {

    }

    override fun getNativeAdList(
        activity: Activity,
        alias: String,
        maxCount: Int,
        listener: NativeListener
    ) {

    }

    override fun isBelongTheProvider(adObject: Any): Boolean {
        return false
    }

    override fun showNativeAd(adObject: Any, container: ViewGroup, nativeView: BaseNativeView) {

    }

    private var mgMobReward: MGMobReward? = null
    override fun requestRewardAd(activity: Activity, alias: String, listener: RewardListener) {
        callbackRewardStartRequest(adProviderType, listener)

        mgMobReward = MGMobReward(activity, object : MGMobRewardListener {
            override fun onClick() {
                callbackRewardClicked(adProviderType, listener)
            }

            override fun onExpose() {

            }

            override fun onLoaded() {
                callbackRewardLoaded(adProviderType, listener)
            }

            override fun onVideoComplete() {

            }

            override fun onError(errorMsg: String?) {
                callbackRewardFailed(adProviderType, listener, errorMsg)
            }

            override fun onClose() {

            }
        })
        mgMobReward?.load()
    }

    override fun showRewardAd(activity: Activity) {
        mgMobReward?.show()
    }

}