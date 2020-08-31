package com.ifmvo.togetherad.demo.banner

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperBanner
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_banner.*

/**
 * Banner 横幅广告
 *
 * Created by Matthew Chen on 2020/5/26.
 */
class BannerActivity : AppCompatActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, BannerActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_banner)

        btnRequestShow.setOnClickListener {
            showBannerAd()
        }

        showBannerAd()
    }

    private fun showBannerAd() {

        adContainer.removeAllViews()

        val radioMapBanner = mapOf(
            AdProviderType.GDT.type to 1,
            AdProviderType.CSJ.type to 1,
            AdProviderType.BAIDU.type to 1
        )

        AdHelperBanner.show(activity = this, alias = TogetherAdAlias.AD_BANNER, container = adContainer, radioMap = radioMapBanner, listener = object : BannerListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                addLog("\n开始请求了，$providerType")
                /*
                 * 百度：设置'广告着陆页'动作栏的颜色主题，目前开放了七大主题：黑色、蓝色、咖啡色、绿色、藏青色、红色、白色(默认) 主题
                 */
//                if (providerType == AdProviderType.BAIDU.type) {
//                    AppActivity.setActionBarColorTheme(AppActivity.ActionBarColorTheme.ACTION_BAR_RED_THEME)
//                }
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                addLog("请求到了，$providerType")
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("单个广告请求失败, $providerType, $failedMsg")
            }

            override fun onAdFailedAll() {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("全部请求失败了")
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
                addLog("点击了，$providerType")
            }

            override fun onAdExpose(providerType: String) {
                //广告展示曝光的回调；由于 Banner 广告存在自动刷新功能，所以曝光会回调多次，每次刷新都会回调
                addLog("曝光了，$providerType")
            }

            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
                addLog("关闭了，$providerType")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        //销毁，避免内存泄漏
        AdHelperBanner.destroy()
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}