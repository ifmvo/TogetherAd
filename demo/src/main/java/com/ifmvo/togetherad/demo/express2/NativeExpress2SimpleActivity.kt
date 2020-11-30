package com.ifmvo.togetherad.demo.express2

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperNativeExpress2
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.core.listener.NativeExpress2ViewListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import com.ifmvo.togetherad.demo.native_.template.NativeExpress2TemplateSimple
import kotlinx.android.synthetic.main.activity_native_express_simple.*

/**
 * Created by Matthew Chen on 2020/11/26.
 */
class NativeExpress2SimpleActivity : AppCompatActivity() {

    private val tag = "NativeExpress2SimpleActivity"

    private var mAdObject: Any? = null

    private val adHelperNativeExpress2 by lazy {
        val ratioMapNativeExpress2 = mapOf(
                AdProviderType.GDT.type to 1,
                AdProviderType.CSJ.type to 1
        )
        AdHelperNativeExpress2(this, TogetherAdAlias.AD_NATIVE_EXPRESS_2_SIMPLE, ratioMapNativeExpress2, 1)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_express_simple)

        btnRequest.setOnClickListener {
            requestAd()
        }

        btnShow.setOnClickListener {
            showAd()
        }
    }

    private fun requestAd() {
        adHelperNativeExpress2.getExpress2List(object : NativeExpress2Listener {
            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                mAdObject = adList[0]

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
        AdHelperNativeExpress2.show(this, mAdObject, adContainer, NativeExpress2TemplateSimple(), object : NativeExpress2ViewListener {
            override fun onAdExposed(providerType: String) {
                //每次展示就会回调这里一次
                addLog("原生模板广告展示了")
                "onAdShow".logi(tag)
            }

            override fun onAdClicked(providerType: String) {
                //每次点击就会回调这里一次
                addLog("原生模板广告点击了")
                "onAdClicked".logi(tag)
            }

            override fun onAdRenderSuccess(providerType: String) {
                //模板渲染成功了就会回调这里一次
                addLog("模板渲染成功了")
                "onAdRenderSuccess".logi(tag)
            }

            override fun onAdRenderFailed(providerType: String) {
                //模板渲染失败了就会回调这里一次
                addLog("模板渲染失败了")
                "onAdRenderFail".logi(tag)
            }

            override fun onAdClose(providerType: String) {
                //模板广告关闭了就会回调这里一次
                addLog("模板广告关闭了")
                "onAdClosed".logi(tag)
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        adHelperNativeExpress2.destroyAllExpress2Ad()
        //或者使用静态方法
//        AdHelperNativeExpress2.destroyExpress2Ad(mAdObject)
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}