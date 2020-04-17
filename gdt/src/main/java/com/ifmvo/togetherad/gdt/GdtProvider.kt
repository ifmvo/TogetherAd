package com.ifmvo.togetherad.gdt

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.helper.AdHelperSplash
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError
import kotlin.math.roundToInt

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
class GdtProvider : BaseAdProvider() {

    private val adProviderType = AdProviderType.GDT

    override fun showSplashAd(activity: Activity, alias: String, radio: String?, container: ViewGroup, listener: SplashListener) {

        callbackStartRequest(adProviderType, listener)

        val customSkipView = AdHelperSplash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        val splash = SplashAD(activity, skipView, TogetherAdGdt.appIdGDT, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {
            override fun onADDismissed() {
                callbackDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                callbackFailed(adProviderType, listener, "错误码: ${adError?.errorCode}, 错误信息：${adError?.errorMsg}")
            }

            /**
             * 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
             */
            override fun onADPresent() {
                activity.runOnUiThread {
                    container.addView(skipView, customSkipView?.getLayoutParams())
                }
                "${adProviderType}: 广告成功展示".logi()
            }

            override fun onADClicked() {
                callbackClicked(adProviderType, listener)
            }

            override fun onADTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000f).roundToInt()
                customSkipView?.handleTime(second)
                "${adProviderType}: 倒计时: $second".logv()
            }

            override fun onADExposure() {
                callbackExposure(adProviderType, listener)
            }

            /**
             * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，否则在showAd时会返回广告超时错误。
             */
            override fun onADLoaded(expireTimestamp: Long) {
                callbackLoaded(adProviderType, listener)
            }
        }, 0)
        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以调用上一个构造方法，或者给 fetchDelay 设为0。
         */
        splash.fetchAndShowIn(container)
    }

    override fun getNativeAdList(activity: Activity, alias: String, radio: String?, listener: SplashListener) {

    }

}