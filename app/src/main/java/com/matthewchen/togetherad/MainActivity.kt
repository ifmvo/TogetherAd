package com.matthewchen.togetherad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.rumtel.ad.helper.flow.TogetherAdFlow
import com.rumtel.ad.helper.inter.TogetherAdInter
import com.rumtel.ad.helper.preMovie.TogetherAdPreMovie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    object MainAct {
        fun action(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preMovieAd()

        mBtnFlow.setOnClickListener {
            flowAd()
        }

        mBtnInter.setOnClickListener {
            interstitialAd()
        }

        mBtnPreMoive.setOnClickListener {
            preMovieAd()
        }

    }

    private fun flowAd() {
        TogetherAdFlow.getAdList(
            this,
            Config.listAdConfig(), TogetherAdConst.AD_FLOW_INDEX, object : TogetherAdFlow.AdListenerList {
                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
                }

                override fun onAdLoaded(channel: String, adList: List<*>) {
                    Log.e("ifmvo", "onAdLoaded:channel:$channel")
                }

                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest:channel:$channel")
                }

            })
    }

    private fun preMovieAd() {
        //baidu:2,gdt:8
        TogetherAdPreMovie.showAdPreMovie(
            this,
            Config.preMoiveAdConfig(),
            TogetherAdConst.AD_TIEPIAN_LIVE,
            ll_ad,
            object : TogetherAdPreMovie.AdListenerPreMovie {
                override fun onAdClick(channel: String) {
                }

                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
                }

                override fun onAdDismissed() {
                    Log.e("ifmvo", "onAdDismissed")
                }

                override fun onAdPrepared(channel: String) {
                    Log.e("ifmvo", "onAdPrepared:channel:$channel")
                }

                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest:channel:$channel")
                }

            })
    }

    private fun interstitialAd() {
        TogetherAdInter.showAdInter(
            this,
            Config.interAdConfig(),
            TogetherAdConst.AD_INTER,
            false,
            mRlInterAd,
            object : TogetherAdInter.AdListenerInter {
                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest:channel:$channel")
                }

                override fun onAdClick(channel: String) {
                    Log.e("ifmvo", "onAdClick:channel:$channel")
                }

                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
                }

                override fun onAdDismissed() {
                    Log.e("ifmvo", "onAdDismissed")
                }

                override fun onAdPrepared(channel: String) {
                    Log.e("ifmvo", "onAdPrepared:channel:$channel")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        TogetherAdPreMovie.destroy()
    }

    private var lastTimeMillis: Long = 0
    override fun finish() {
        if (System.currentTimeMillis() - lastTimeMillis >= 1500) {
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show()
            lastTimeMillis = System.currentTimeMillis()
            return
        }
        super.finish()
    }
}
