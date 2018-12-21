package com.matthewchen.togetherad

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.rumtel.ad.AdHelperInterstitial
import com.rumtel.ad.AdHelperPreMovie
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

        mBtnInter.setOnClickListener {
            interstitialAd()
        }

        mBtnPreMoive.setOnClickListener {
            preMovieAd()
        }
    }

    private fun preMovieAd() {
        //baidu:2,gdt:8
        AdHelperPreMovie.showAdPreMovie(this, Config.preMoiveAdConfig(), ll_ad,
            object : AdHelperPreMovie.AdListenerPreMovie {
                override fun onAdClick(channel: String) {
                    Log.e("ifmvo", "onAdClick")
                }

                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed")
                }

                override fun onAdDismissed() {
                    Log.e("ifmvo", "onAdDismissed")
                }

                override fun onAdPrepared(channel: String) {
                    Log.e("ifmvo", "onAdPrepared")
                }

                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest")
                }
            })
    }

    private fun interstitialAd() {
        AdHelperInterstitial.showAdInterstitial(this, Config.interAdConfig(), false, mRlInterAd,
            object : AdHelperInterstitial.AdListenerInterstitial {
                override fun onStartRequest(channel: String?) {
                }

                override fun onAdClick(channel: String?) {
                }

                override fun onAdFailed(failedMsg: String?) {
                }

                override fun onAdDismissed() {
                }

                override fun onAdPrepared(channel: String?) {
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        AdHelperPreMovie.destroy()
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
