package com.ifmvo.togetherad.demo.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.custom.splashSkip.SplashSkipViewSimple2
import com.ifmvo.togetherad.core.helper.AdHelperSplash
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_splash.*

/* 
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

        AdHelperSplash.customSkipView = SplashSkipViewSimple2()

        AdHelperSplash.show(activity = this, alias = TogetherAdAlias.AD_SPLASH, container = adContainer, listener = object : SplashListener {
            override fun onAdStartRequest(providerType: String) {
                addLog("开屏广告开始请求，$providerType")
                "onAdStartRequest: $providerType".logi(TAG)
            }

            override fun onAdLoaded(providerType: String) {
                addLog("开屏广告请求好了，$providerType")
                "onAdLoaded: $providerType".logi(TAG)
            }

            override fun onAdClicked(providerType: String) {
                addLog("开屏广告被点击了，$providerType")
                "onAdClicked: $providerType".logi(TAG)
            }

            override fun onAdExposure(providerType: String) {
                addLog("开屏广告曝光了，$providerType")
                "onAdExposure: $providerType".logi(TAG)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                addLog("开屏广告单个提供商请求失败了，$providerType")
                "onAdFailed: $providerType: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                addLog(failedMsg)
                "onAdFailedAll: $failedMsg".loge(TAG)
                actionHome(1000)
            }

            override fun onAdDismissed(providerType: String) {
                addLog("开屏广告点了跳过或者倒计时结束， $providerType")
                "onAdDismissed: $providerType".logi(TAG)
                actionHome(0)
            }
        })
    }

    private fun actionHome(delayMillis: Long) {
        adContainer.postDelayed({
            //在这里跳转到 Home 主界面xxxxx
            finish()
        }, delayMillis)
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}