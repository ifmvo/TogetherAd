package com.ifmvo.togetherad.demo.reward

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.ifmvo.togetherad.core.helper.AdHelperReward
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.csj.CsjProvider
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import com.ifmvo.togetherad.gdt.GdtProvider
import kotlinx.android.synthetic.main.activity_reward.*

/**
 * 激励广告使用实例
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

        //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
        val radioMapReward = mapOf(
                AdProviderType.GDT.type to 0,
                AdProviderType.CSJ.type to 1,
                AdProviderType.BAIDU.type to 0
        )

        /**
         * 穿山甲请求激励广告可选参数
         * 以下参数均为（ 非必传 ）
         */
//        CsjProvider.Reward.userID = "userId_123"
//        CsjProvider.Reward.supportDeepLink = true//默认为true
//        CsjProvider.Reward.rewardName = "金币"//奖励名称
//        CsjProvider.Reward.rewardAmount = 30//奖励数量
//        CsjProvider.Reward.mediaExtra = "TogetherAd"//用户透传的信息
//        //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL。默认是TTAdConstant.VERTICAL
//        CsjProvider.Reward.orientation = TTAdConstant.VERTICAL//默认 TTAdConstant.VERTICAL
//        CsjProvider.Reward.showDownLoadBar = true//不设置默认为true

        /**
         * activity: 必传。
         * alias: 必传。广告位的别名。初始化的时候是根据别名设置的广告ID，所以这里TogetherAd会根据别名查找对应的广告位ID。
         * radioMap: 非必传。广告商的权重。可以不传或传null，空的情况 TogetherAd 会自动使用初始化时 TogetherAd.setPublicProviderRadio 设置的全局通用权重。
         * listener: 非必传。如果你不需要监听结果可以不传或传空。各个回调方法也可以选择性添加
         */
        adHelperReward = AdHelperReward(activity = this, alias = TogetherAdAlias.AD_REWARD, radioMap = radioMapReward, listener = object : RewardListener {
            override fun onAdStartRequest(providerType: String) {
                //在开始请求之前会回调此方法，失败切换的情况会回调多次
                addLog("\n开始请求: $providerType")
                "onAdStartRequest: $providerType".logi(TAG)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //请求失败的回调，失败切换的情况会回调多次
                addLog("请求失败: $providerType")
                "onAdFailed: $providerType: $failedMsg".loge(TAG)
            }

            override fun onAdFailedAll() {
                //所有配置的广告商都请求失败了，只有在全部失败之后会回调一次
                addLog("全部失败")
                "onAdFailedAll".loge(TAG)
            }

            override fun onAdClicked(providerType: String) {
                //点击广告的回调
                addLog("点击了: $providerType")
                "onAdClicked: $providerType".logi(TAG)
            }

            override fun onAdShow(providerType: String) {
                //广告展示展示的回调
                addLog("展示了: $providerType")
                "onAdShow: $providerType".logi(TAG)
            }

            override fun onAdLoaded(providerType: String) {
                //广告请求成功的回调，每次请求只回调一次
                addLog("请求到了: $providerType")
                "onAdLoaded: $providerType".logi(TAG)
            }

            override fun onAdExpose(providerType: String) {
                //广告展示曝光的回调
                addLog("曝光了: $providerType")
                "onAdExpose: $providerType".logi(TAG)
            }

            override fun onAdVideoComplete(providerType: String) {
                //视频播放完成的回调
                addLog("视频播放完成: $providerType")
                "onAdVideoComplete: $providerType".logi(TAG)
            }

            override fun onAdVideoCached(providerType: String) {
                //视频缓存完成的回调
                addLog("视频已缓存: $providerType")
                "onAdVideoCached: $providerType".logi(TAG)
            }

            override fun onAdRewardVerify(providerType: String) {
                //激励结果验证成功的回调
                addLog("激励验证，$providerType")
                "onAdRewardVerify，$providerType".logi(TAG)

//                //如果 csj 使用到服务器到服务器回调功能，需要进行以下判断，gdt、baidu都不需要此判断
//                if (providerType == AdProviderType.CSJ.type && CsjProvider.Reward.rewardVerify) {
//                    ... 验证成功后的操作
//                }
            }

            override fun onAdClose(providerType: String) {
                //广告被关闭的回调
                addLog("关闭了: $providerType")
                "onAdClose: $providerType".logi(TAG)
            }
        })

        btnRequest.setOnClickListener {
            //开始请求广告
            adHelperReward.load()
        }

        btnShow.setOnClickListener {
            //onAdLoaded 回调之后才能展示
            adHelperReward.show()
        }
    }

    private var logStr = "日志: \n"

    private fun addLog(content: String?) {
        logStr = logStr + content + "\n"
        log.text = logStr

        info.post { info.fullScroll(View.FOCUS_DOWN) }
    }
}