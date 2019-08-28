package com.matthewchen.togetherad.ui

import android.Manifest
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
import com.matthewchen.togetherad.utils.Kits
import com.rumtel.ad.helper.splash.TogetherAdSplash
import kotlinx.android.synthetic.main.activity_splash.*
import kr.co.namee.permissiongen.PermissionFail
import kr.co.namee.permissiongen.PermissionGen
import kr.co.namee.permissiongen.PermissionSuccess

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/21.
 */
class SplashActivity : AppCompatActivity() {

    private var isPermission = false

    private var canJumpImmediately = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Kits.StatuBar.immersive(this)
        Kits.StatuBar.darkMode(this)
        Kits.StatuBar.setPaddingSmart(this, mFlAdContainer)

        PermissionGen.with(this)
            .addRequestCode(100)
            .permissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request()

    }

    override fun onPause() {
        super.onPause()
        canJumpImmediately = false
    }

    override fun onResume() {
        super.onResume()
        if (canJumpImmediately) {
            actionHome(0)
        }
        canJumpImmediately = true
    }

    /**
     * 权限申请成功
     */
    @PermissionSuccess(requestCode = 100)
    fun permissionSuccess() {
        isPermission = true
        requestAd()
    }

    /**
     * 权限申请失败
     */
    @PermissionFail(requestCode = 100)
    fun permissionFail() {
        isPermission = false
        requestAd()
    }

    /**
     * 跳转 Main, 延迟多少毫秒
     */
    private fun actionHome(delayMillis: Long) {
      /*  mFlAdContainer.postDelayed({
            MainActivity.MainAct.action(this)
            finish()
        }, delayMillis)*/
    }

    override fun onBackPressed() {
        if (!isPermission) {
            super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults)
    }

    private fun requestAd() {
        val splashConfigAd = Config.splashAdConfig()
        TogetherAdSplash.showAdFull(
            this,
            splashConfigAd,
            TogetherAdConst.AD_SPLASH,
            mFlAdContainer,skip_view,
            object : TogetherAdSplash.AdListenerSplashFull {
                override fun onStartRequest(channel: String) {
                    Log.e("ifmvo", "onStartRequest:channel:$channel")
                }

                override fun onAdClick(channel: String) {
                    Log.e("ifmvo", "onAdClick:channel:$channel")
                }

                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed:failedMsg:$failedMsg")
                    actionHome(0)
                }

                override fun onAdDismissed() {
                    Log.e("ifmvo", "onAdDismissed")
                    if (canJumpImmediately) {
                        actionHome(0)
                    }
                    canJumpImmediately = true
                }

                override fun onAdPrepared(channel: String) {
                    Log.e("ifmvo", "onAdPrepared:channel:$channel")
                }
            })
    }
}