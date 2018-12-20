package com.matthewchen.togetherad

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rumtel.ad.AdHelperPreMovie
import kotlinx.android.synthetic.main.activity_main.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        PermissionGen.with(this)
            .addRequestCode(100)
            .permissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request()

        btn.setOnClickListener {
            splashAd()
        }
    }

    /**
     * 权限申请成功
     */
    @PermissionSuccess(requestCode = 100)
    fun permissionSuccess() {
        splashAd()
    }

    /**
     * 权限申请失败
     */
    @PermissionFail(requestCode = 100)
    fun permissionFail() {
    }

    private fun splashAd() {
        //baidu:2,gdt:8
        AdHelperPreMovie.showAdPreMovie(this, "baidu:5,gdt:5", ll_ad, object : AdHelperPreMovie.AdListenerPreMovie {
            override fun onAdClick(channel: String) {
            }

            override fun onAdFailed(failedMsg: String?) {
            }

            override fun onAdDismissed() {
            }

            override fun onAdPrepared(channel: String) {
            }

            override fun onStartRequest(channel: String) {
            }
        })

//        AdHelperInterstitial.showAdInterstitial(
//            this,
//            "baidu:5,gdt:5",
//            false,
//            ll_ad,
//            object : AdHelperInterstitial.AdListenerInterstitial {
//                override fun onStartRequest(channel: String?) {
//                }
//
//                override fun onAdClick(channel: String?) {
//                }
//
//                override fun onAdFailed(failedMsg: String?) {
//                }
//
//                override fun onAdDismissed() {
//                }
//
//                override fun onAdPrepared(channel: String?) {
//                }
//            })
//        AdHelperSplashFull.showAdFull(this, "baidu:5,gdt:5", ll_ad, object : AdHelperSplashFull.AdListenerSplashFull {
//            override fun onStartRequest(channel: String?) {
//                Log.e("ifmvo", "onStartRequest" + channel)
//            }
//
//            override fun onAdClick(channel: String?) {
//                Log.e("ifmvo", channel)
//            }
//
//            override fun onAdFailed(failedMsg: String?) {
//                Log.e("ifmvo", "onAdFailed" + failedMsg)
//            }
//
//            override fun onAdDismissed() {
//                Log.e("ifmvo", "onAdDismissed")
//            }
//
//            override fun onAdPrepared(channel: String?) {
//                Log.e("ifmvo", "onAdPrepared" + channel)
//            }
//
//        })
    }
}
