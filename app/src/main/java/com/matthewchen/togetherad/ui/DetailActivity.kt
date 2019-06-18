package com.matthewchen.togetherad.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
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

    }

    private fun preMovieAd() {
        //baidu:2,gdt:8
        TogetherAdPreMovie.showAdPreMovie(this, Config.preMoiveAdConfig(), TogetherAdConst.AD_TIEPIAN_LIVE, ll_ad,
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

//        val adPlaceId = "2543741" // 重要：请填上您的广告位ID，代码位错误会导致无法请求到广告
//        interAd = InterstitialAd(this, adPlaceId)
//        interAd.setListener(object : InterstitialAdListener {
//
//            override fun onAdClick(arg0: InterstitialAd) {
//                Log.i("InterstitialAd", "onAdClick")
//            }
//
//            override fun onAdDismissed() {
//                Log.i("InterstitialAd", "onAdDismissed")
//            }
//
//            override fun onAdFailed(arg0: String) {
//                Log.i("InterstitialAd", "onAdFailed")
//            }
//
//            override fun onAdPresent() {
//                Log.i("InterstitialAd", "onAdPresent")
//            }
//
//            override fun onAdReady() {
//                Log.i("InterstitialAd", "onAdReady")
//                interAd.showAd(this@DetailActivity)
//            }
//        })

//        TogetherAd.request {
//
//            ratio(gdt = 1, baidu = 2, xunfei = 3)
//            adConst(TogetherAdConst.AD_INTER)
//
//            listen(object : TogetherAdInter.AdListenerInter {
//                override fun onStartRequest(channel: String) {
//                    Log.e("ifmvo", "onStartRequest:channel:$channel")
//                }
//
//                override fun onAdClick(channel: String) {
//                    Log.e("ifmvo", "onAdClick:channel:$channel")
//                }
//
//                override fun onAdFailed(failedMsg: String?) {
//                    Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
//                }
//
//                override fun onAdDismissed() {
//                    Log.e("ifmvo", "onAdDismissed")
//                }
//
//                override fun onAdPrepared(channel: String) {
//                    Log.e("ifmvo", "onAdPrepared:channel:$channel")
//                }
//            })
//        }


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

    override fun onDestroy() {
        super.onDestroy()
        TogetherAdPreMovie.destroy()
    }
}
