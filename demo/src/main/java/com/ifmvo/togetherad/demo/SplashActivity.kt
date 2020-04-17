package com.ifmvo.togetherad.demo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.custom.splashSkip.Simple1SplashSkipView
import com.ifmvo.togetherad.core.helper.AdHelperSplash
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import kotlinx.android.synthetic.main.activity_splash.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, SplashActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        requestSplashAd()
    }

    /**
     * 请求开屏广告
     */
    private fun requestSplashAd() {

        AdHelperSplash.customSkipView = Simple1SplashSkipView()

        AdHelperSplash.show(activity = this, alias = TogetherAdAlias.AD_SPLASH, container = adContainer, listener = object : SplashListener {
            override fun onAdStartRequest(providerType: AdProviderType) {
                "onAdStartRequest: ${providerType.type}".logi(TAG)
            }

            override fun onAdLoaded(providerType: AdProviderType) {
                "onAdLoaded: ${providerType.type}".logi(TAG)
            }

            override fun onAdClicked(providerType: AdProviderType) {
                "onAdClicked: ${providerType.type}".logi(TAG)
            }

            override fun onAdExposure(providerType: AdProviderType) {
                "onAdExposure: ${providerType.type}".logi(TAG)
            }

            override fun onAdFailed(providerType: AdProviderType, failedMsg: String?) {
                "onAdFailed: ${providerType.type}: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                "onAdFailedAll: $failedMsg".loge(TAG)
                actionHome(1000)
            }

            override fun onAdDismissed(providerType: AdProviderType) {
                "onAdDismissed: ${providerType.type}".logi(TAG)
                actionHome(0)
            }
        })
    }

    private fun actionHome(delayMillis: Long) {
        adContainer.postDelayed({
            //在这里跳转到 Home 主界面
            finish()
        }, delayMillis)
    }
}