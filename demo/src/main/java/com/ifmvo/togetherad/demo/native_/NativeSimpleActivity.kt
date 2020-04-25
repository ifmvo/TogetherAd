package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.helper.AdHelperNative
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_flow_simple.*

/* 
 * (●ﾟωﾟ●)
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

        AdHelperNative.getList(activity = this, alias = TogetherAdAlias.AD_FLOW_INDEX, maxCount = 10, listener = object : NativeListener {
            override fun onAdStartRequest(providerType: AdProviderType) {
                "onAdStartRequest: ${providerType.type}".logi(TAG)
            }

            override fun onAdLoaded(providerType: AdProviderType, adList: List<Any>) {
                "onAdLoaded: ${providerType.type}, adList: ${adList.size}".logi(TAG)
                AdHelperNative.show(adList[0], adContainer, NativeTemplateCommon())
            }

            override fun onAdFailed(providerType: AdProviderType, failedMsg: String?) {
                "onAdFailed: ${providerType.type}: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                "onAdFailedAll: $failedMsg".loge(TAG)
            }
        })
    }
}