package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.bytedance.sdk.openadsdk.AdSlot
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_flow_simple.*

/**
 * 原生自渲染的简单用法
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeSimpleActivity : AppCompatActivity() {

    private val TAG = "NativeSimpleActivity"

    //声明
    private var adHelperNative: AdHelperNativePro? = null

    private var mAdObject: Any? = null

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

        //初始化
        adHelperNative = AdHelperNativePro(activity = this, alias = TogetherAdAlias.AD_NATIVE_SIMPLE, maxCount = 1/*, radioMap = radioMapNativeSimple*/)

        btnRequestShow.setOnClickListener {
            requestAd()
        }

        requestAd()
    }

    /**
     * 请求广告
     */
    private fun requestAd() {
        //--------------------------------------------------------------------------------------
        //  必须在每次请求穿山甲的原生广告之前设置类型。
        //  设置方式：AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FEED
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_INTERACTION_AD
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_BANNER
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_CACHED_SPLASH
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_DRAW_FEED
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_REWARD_VIDEO
        //  AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_SPLASH
        //--------------------------------------------------------------------------------------
        AdHelperNativePro.csjNativeAdType = AdSlot.TYPE_FEED

        adHelperNative?.getList(listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                "onAdStartRequest: $providerType".logi(TAG)
                addLog("\n原生广告开始请求，$providerType")
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                //广告请求成功的回调，每次请求只回调一次
                "onAdLoaded: $providerType, adList: ${adList.size}".logi(TAG)
                addLog("原生广告请求成功，$providerType")
                mAdObject = adList[0]
                showAd(adList[0])
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("原生广告单个提供商请求失败了，$providerType, $failedMsg")
                "onAdFailed: $providerType: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll() {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("原生广告全部请求失败了")
                "onAdFailedAll".loge(TAG)
            }
        })
    }

    /**
     * 展示广告
     */
    private fun showAd(adObject: Any) {
        adContainer.removeAllViews()
        AdHelperNativePro.show(adObject = adObject, container = adContainer, nativeTemplate = NativeTemplateCommon(), listener = object : NativeViewListener {
            override fun onAdExposed(providerType: String) {
                //每次曝光就会回调这里一次
                addLog("原生广告曝光了")
                "onAdExposed".logi(TAG)
            }

            override fun onAdClicked(providerType: String) {
                //每次点击就会回调这里一次
                addLog("原生广告点击了")
                "onAdClicked".logi(TAG)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        mAdObject?.run {
            AdHelperNativePro.pauseAd(this)
        }
    }

    override fun onResume() {
        super.onResume()
        mAdObject?.run {
            AdHelperNativePro.resumeAd(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mAdObject?.run {
            AdHelperNativePro.destroyAd(this)
        }
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}