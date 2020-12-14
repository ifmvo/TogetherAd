package com.ifmvo.togetherad.demo.hybrid

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import com.ifmvo.togetherad.demo.hybrid.helper.AdHelperHybridExpress
import kotlinx.android.synthetic.main.activity_native_express_simple.*

/**
 * Created by Matthew Chen on 2020/12/1.
 */
class NativeExpressHybridActivity : AppCompatActivity() {

    private val tag = "NativeExpressHybridActivity"

    private var mAdObject: Any? = null
    private var mProviderType: String? = null

    private val adHelperHybridExpress by lazy {
        val ratioMapExpressHybrid = linkedMapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.CSJ.type to 1
        )
        AdHelperHybridExpress(this, TogetherAdAlias.AD_HYBRID_EXPRESS, ratioMapExpressHybrid, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_express_hybrid)

        btnRequest.setOnClickListener {
            requestAd()
        }

        btnShow.setOnClickListener {
            showAd()
        }
    }

    private fun requestAd() {
        adHelperHybridExpress.getHybridExpressList(object : NativeExpress2Listener {
            override fun onAdLoaded(providerType: String, adList: List<Any>) {

                mAdObject = adList[0]
                mProviderType = providerType

                //广告请求成功的回调，每次请求只回调一次
                "onAdLoaded: $providerType, adList: ${adList.size}".logi(tag)
                addLog("原生模板广告请求成功，$providerType")
            }

            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                "onAdStartRequest: $providerType".logi(tag)
                addLog("\n原生模板广告开始请求，$providerType")
            }

            override fun onAdFailedAll() {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("原生模板广告全部请求失败了")
                "onAdFailedAll".loge(tag)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("原生模板广告单个提供商请求失败了，$providerType, $failedMsg")
                "onAdFailed: $providerType: $failedMsg".loge(tag)
            }
        })
    }

    private fun showAd() {
        AdHelperHybridExpress.show(mProviderType, this, mAdObject, adContainer)
    }

    override fun onDestroy() {
        super.onDestroy()
        adHelperHybridExpress.destroyAllHybridExpressAd()
        //或者
//        AdHelperHybridExpress.destroyHybridExpressAd(mAdObject)
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}