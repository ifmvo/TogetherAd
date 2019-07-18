package com.matthewchen.togetherad.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
import com.rumtel.ad.helper.banner.TogetherAdBanner
import com.rumtel.ad.helper.banner.TogetherAdBanner2
import com.rumtel.ad.helper.inter.TogetherAdInter
import com.rumtel.ad.helper.preMovie.TogetherAdPreMovie
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    object DetailAct {
        fun action(context: Context) {
            val intent = Intent(context, DetailActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        mBtnPreMoive.setOnClickListener {
            preMovieAd()
        }

        mBtnInter.setOnClickListener {
            interstitialAd()
        }

        mBtnWebPlay.setOnClickListener {
            webViewBanner()
        }

        mBtnWebPlay2.setOnClickListener {
            webViewBanner2()
        }
    }

    private fun preMovieAd() {
        //baidu:2,gdt:8
        TogetherAdPreMovie.showAdPreMovie(this, Config.preMoiveAdConfig(), TogetherAdConst.AD_TIEPIAN_LIVE, ll_ad,
            object : TogetherAdPreMovie.AdListenerPreMovie {
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

                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest:channel:$channel")
                }
            }, needTimer = false
        )
    }

    private fun interstitialAd() {

        TogetherAdInter.showAdInter(this, Config.interAdConfig(), TogetherAdConst.AD_INTER, false, mRlInterAd,
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

    private fun webViewBanner() {
        TogetherAdBanner.requestBanner(this, Config.webViewAdConfig(), TogetherAdConst.AD_WEBVIEW_BANNER,
            mFlAdBannerContainer, object : TogetherAdBanner.AdListenerList {
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


    private fun webViewBanner2() {
        TogetherAdBanner2.requestBanner(this, Config.webViewAdConfig(), TogetherAdConst.AD_WEBVIEW_BANNER,
            mFlAdBannerContainer, object : TogetherAdBanner2.AdListenerList {
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
}
