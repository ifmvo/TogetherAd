package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.helper.AdHelperNative
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_flow_simple.*

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeSimpleActivity : AppCompatActivity() {

    private val TAG = "NativeSimpleActivity"

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeSimpleActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_simple)

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val radioMapNativeSimple = mapOf(
                AdProviderType.GDT.type to 3,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 1
        )

        AdHelperNative.getList(activity = this, alias = TogetherAdAlias.AD_NATIVE_SIMPLE, maxCount = 1, /*radioMap = radioMapNativeSimple,*/ listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                "onAdStartRequest: $providerType".logi(TAG)
                addLog("原生广告开始请求，$providerType")
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                "onAdLoaded: $providerType, adList: ${adList.size}".logi(TAG)
                addLog("原生广告请求成功，$providerType")
                AdHelperNative.show(adObject = adList[0], container = adContainer, nativeTemplate = NativeTemplateCommon())
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                addLog("原生广告请求失败，$providerType")
                "onAdFailed: $providerType: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                addLog("原生广告全部请求失败了，$failedMsg")
                "onAdFailedAll: $failedMsg".loge(TAG)
            }
        })
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}