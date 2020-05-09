package com.ifmvo.togetherad.demo.reward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core.helper.AdHelperReward
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_reward.*

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-22.
 */
class RewardActivity : AppCompatActivity() {

    private val TAG = "RewardActivity"

    private lateinit var adHelperReward: AdHelperReward

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, RewardActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        adHelperReward = AdHelperReward(activity = this, alias = TogetherAdAlias.AD_REWARD, listener = object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                addLog("\n开始请求: $providerType")
                "onAdStartRequest: $providerType".logi(TAG)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                addLog("请求失败: $providerType")
                "onAdFailed: $providerType: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll(failedMsg: String?) {
                addLog("全部失败: $failedMsg")
                "onAdFailedAll: $failedMsg".loge(TAG)
            }

            override fun onAdClicked(providerType: String) {
                addLog("点击了: $providerType")
                "onAdClicked: $providerType".logi(TAG)
            }

            override fun onAdShow(providerType: String) {
                addLog("展示了: $providerType")
                "onAdShow: $providerType".logi(TAG)
            }

            override fun onAdLoaded(providerType: String) {
                addLog("请求到了: $providerType")
                "onAdLoaded: $providerType".logi(TAG)
            }

            override fun onAdExpose(providerType: String) {
                addLog("曝光了: $providerType")
                "onAdExpose: $providerType".logi(TAG)
            }

            override fun onAdVideoComplete(providerType: String) {
                addLog("视频播放完成: $providerType")
                "onAdVideoComplete: $providerType".logi(TAG)
            }

            override fun onAdVideoCached(providerType: String) {
                addLog("视频已缓存: $providerType")
                "onAdVideoCached: $providerType".logi(TAG)
            }

            override fun onAdRewardVerify(providerType: String) {
                addLog("激励验证: $providerType")
                "onAdRewardVerify: $providerType".logi(TAG)
            }

            override fun onAdClose(providerType: String) {
                addLog("关闭了: $providerType")
                "onAdClose: $providerType".logi(TAG)
            }
        })

        load.setOnClickListener {
            adHelperReward.load()
        }

        show.setOnClickListener {
            adHelperReward.show()
        }
    }

    private var logStr = ""

    private fun addLog(content: String) {
        logStr = logStr + content + "\n"
        log.text = logStr
    }
}