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

    private val adHelperReward by lazy { AdHelperReward() }

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, RewardActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        load.setOnClickListener {
            adHelperReward.load(activity = this, alias = TogetherAdAlias.AD_REWARD, listener = object : RewardListener {
                override fun onAdStartRequest(providerType: String) {
                    addLog("onAdStartRequest: $providerType")
                    "onAdStartRequest: $providerType".logi(TAG)
                }

                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    addLog("onAdStartRequest: $providerType")
                    "onAdFailed: $providerType: $failedMsg".loge(TAG)
                }

                override fun onAdFailedAll(failedMsg: String?) {
                    addLog("onAdFailedAll: $failedMsg")
                    "onAdFailedAll: $failedMsg".loge(TAG)
                }

                override fun onAdClicked(providerType: String) {
                    addLog("onAdClicked: $providerType")
                    "onAdClicked: $providerType".logi(TAG)
                }

                override fun onAdShow(providerType: String) {
                    addLog("onAdShow: $providerType")
                    "onAdShow: $providerType".logi(TAG)
                }
            })
        }

        show.setOnClickListener {
            adHelperReward.show(activity = this)
        }
    }

    private var logStr = ""

    private fun addLog(content: String) {
        logStr = content + "\n" + logStr
        log.text = logStr
    }
}