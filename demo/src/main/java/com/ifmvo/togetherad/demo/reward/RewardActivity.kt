package com.ifmvo.togetherad.demo.reward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ifmvo.togetherad.core._enum.AdProviderType
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

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, RewardActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reward)

        load.setOnClickListener {
            AdHelperReward.load(activity = this, alias = TogetherAdAlias.AD_REWARD, listener = object : RewardListener {
                override fun onAdStartRequest(providerType: AdProviderType) {
                    "onAdStartRequest: ${providerType.type}".logi(TAG)
                }

                override fun onAdFailed(providerType: AdProviderType, failedMsg: String?) {
                    "onAdFailed: ${providerType.type}: $failedMsg".loge(TAG)
                }

                override fun onAdFailedAll(failedMsg: String?) {
                    "onAdFailedAll: $failedMsg".loge(TAG)
                }

                override fun onAdClicked(providerType: AdProviderType) {
                    "onAdClicked: ${providerType.type}".logi(TAG)
                }

                override fun onAdShow(providerType: AdProviderType) {
                    "onAdShow: ${providerType.type}".logi(TAG)
                }
            })
        }

        show.setOnClickListener {

        }
    }

}