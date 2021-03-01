package com.ifmvo.togetherad.demo.hybrid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import com.ifmvo.togetherad.demo.hybrid.helper.AdHelperHybridVerticalPreMovie
import kotlinx.android.synthetic.main.activity_vertical_premovie.*

/**
 * Created by Matthew Chen on 2020/12/1.
 */
class VerticalPreMovieHybridActivity : AppCompatActivity() {

    private val tag = "VerticalPreMovieHybridActivity"

    private val adHelper by lazy {
        val ratioMapVerticalPreMovie = linkedMapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.CSJ.type to 1
        )
        AdHelperHybridVerticalPreMovie(this, TogetherAdAlias.AD_HYBRID_VERTICAL_PREMOVIE, adContainer/*, ratioMapVerticalPreMovie*/)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vertical_premovie)

        btnRequestAndShow.setOnClickListener {
            requestAd()
        }
    }

    private fun requestAd() {

        adHelper.loadAndShow(object : AdHelperHybridVerticalPreMovie.VerticalPreMovieListener {
            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                "onAdLoaded: $providerType".logi(tag)
                addLog("竖屏前贴广告请求成功，$providerType")
            }

            override fun onAdClicked(providerType: String) {
                "onAdClicked: $providerType".logi(tag)
                addLog("竖屏前贴广告点击了，$providerType")
            }

            override fun onAdExpose(providerType: String) {
                "onAdExpose: $providerType".logi(tag)
                addLog("竖屏前贴广告曝光了，$providerType")
            }

            override fun onAdClose(providerType: String) {
                "onAdClose: $providerType".logi(tag)
                addLog("竖屏前贴广告关闭了，$providerType")
            }

            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                "onAdStartRequest: $providerType".logi(tag)
                addLog("\n竖屏前贴广告开始请求，$providerType")
            }

            override fun onAdFailedAll(failedMsg: String?) {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("竖屏前贴广告全部请求失败了")
                "onAdFailedAll".loge(tag)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("竖屏前贴广告单个提供商请求失败了，$providerType, $failedMsg")
                "onAdFailed: $providerType: $failedMsg".loge(tag)
            }
        })

    }

    override fun onResume() {
        super.onResume()
        adHelper.onResume()
    }

    override fun onPause() {
        super.onPause()
        adHelper.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        adHelper.onDestroy()
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}