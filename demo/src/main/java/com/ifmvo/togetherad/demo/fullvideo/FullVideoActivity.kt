package com.ifmvo.togetherad.demo.fullvideo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperFullVideo
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_reward.*

/**
 *
 * Created by Matthew Chen on 2020/12/2.
 */
class FullVideoActivity : AppCompatActivity() {

    private val tag = "FullVideoActivity"

    private lateinit var adHelperFullVideo: AdHelperFullVideo

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, FullVideoActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val ratioMapFullVideo = linkedMapOf(AdProviderType.CSJ.type to 1)

        /**
         * activity: 必传。
         * alias: 必传。广告位的别名。初始化的时候是根据别名设置的广告ID，所以这里TogetherAd会根据别名查找对应的广告位ID。
         * ratioMap: 非必传。广告商的权重。可以不传或传null，空的情况 TogetherAd 会自动使用初始化时 TogetherAd.setPublicProviderRatio 设置的全局通用权重。
         * listener: 非必传。如果你不需要监听结果可以不传或传空。各个回调方法也可以选择性添加
         */
        adHelperFullVideo = AdHelperFullVideo(activity = this, alias = TogetherAdAlias.AD_FULL_VIDEO, /*ratioMap = ratioMapFullVideo,*/ listener = object : FullVideoListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                addLog("\n开始请求: $providerType")
                "onAdStartRequest: $providerType".logi(tag)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("请求失败: $providerType")
                "onAdFailed: $providerType: $failedMsg".loge(tag)
            }

            override fun onAdFailedAll() {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("全部失败")
                "onAdFailedAll".loge(tag)
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
                addLog("点击了: $providerType")
                "onAdClicked: $providerType".logi(tag)
            }

            override fun onAdShow(providerType: String) {
                //广告展示展示的回调
                addLog("展示了: $providerType")
                "onAdShow: $providerType".logi(tag)
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                addLog("请求到了: $providerType")
                "onAdLoaded: $providerType".logi(tag)
            }

            override fun onAdVideoComplete(providerType: String) {
                //视频播放完成的回调
                addLog("视频播放完成: $providerType")
                "onAdVideoComplete: $providerType".logi(tag)
            }

            override fun onAdVideoCached(providerType: String) {
                //视频缓存完成的回调
                addLog("视频已缓存: $providerType")
                "onAdVideoCached: $providerType".logi(tag)
            }

            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
                addLog("关闭了: $providerType")
                "onAdClose: $providerType".logi(tag)
            }
        })

        btnRequest.setOnClickListener {
            //开始请求广告
            adHelperFullVideo.load()
        }

        btnShow.setOnClickListener {
            //onAdLoaded 回调之后才能展示
            adHelperFullVideo.show()
        }
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}