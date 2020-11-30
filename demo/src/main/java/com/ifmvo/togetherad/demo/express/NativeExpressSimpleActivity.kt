package com.ifmvo.togetherad.demo.express

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperNativeExpress
import com.ifmvo.togetherad.core.listener.NativeExpressListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import com.ifmvo.togetherad.demo.native_.template.NativeExpressTemplateSimple
import kotlinx.android.synthetic.main.activity_native_express_simple.*

/**
 * Created by Matthew Chen on 2020/11/26.
 */
class NativeExpressSimpleActivity : AppCompatActivity() {

    private val tag = "NativeExpressSimpleActivity"

    private var mAdObject: Any? = null

    private val adHelperNativeExpress by lazy {
        val ratioMapNativeExpress = mapOf(AdProviderType.GDT.type to 1)
        AdHelperNativeExpress(this, TogetherAdAlias.AD_NATIVE_EXPRESS_SIMPLE, ratioMapNativeExpress, 1)
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
        adHelperNativeExpress.getExpressList(object : NativeExpressListener {
            override fun onAdLoaded(providerType: String, adList: List<Any>) {

                mAdObject = adList[0]

                //广告请求成功的回调，每次请求只回调一次
                "onAdLoaded: $providerType, adList: ${adList.size}".logi(tag)
                addLog("原生模板广告请求成功，$providerType")
            }

            override fun onAdClicked(providerType: String, adObject: Any?) {
                //每次点击就会回调这里一次
                addLog("原生模板广告点击了")
                "onAdClicked".logi(tag)
            }

            override fun onAdShow(providerType: String, adObject: Any?) {
                //每次展示就会回调这里一次
                addLog("原生模板广告展示了")
                "onAdShow".logi(tag)
            }

            override fun onAdRenderSuccess(providerType: String, adObject: Any?) {
                //模板渲染成功了就会回调这里一次
                addLog("模板渲染成功了")
                "onAdRenderSuccess".logi(tag)
            }

            override fun onAdRenderFail(providerType: String, adObject: Any?) {
                //模板渲染失败了就会回调这里一次
                addLog("模板渲染失败了")
                "onAdRenderFail".logi(tag)
            }

            override fun onAdClosed(providerType: String, adObject: Any?) {
                //模板广告关闭了就会回调这里一次
                addLog("模板广告关闭了")
                "onAdClosed".logi(tag)
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
        AdHelperNativeExpress.show(mAdObject, adContainer, NativeExpressTemplateSimple())
    }

    override fun onDestroy() {
        super.onDestroy()
        adHelperNativeExpress.destroyAllExpressAd()
        //或者
//        AdHelperNativeExpress.destroyExpressAd(mAdObject)
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}