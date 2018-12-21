package com.matthewchen.togetherad

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.rumtel.ad.AdHelperSplashFull
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

        PermissionGen.with(this)
            .addRequestCode(100)
            .permissions(
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .request()

    }

    override fun onRestart() {
        super.onRestart()
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
        showPermissionDialog()
    }

    private fun showPermissionDialog() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("请同意广告需要的权限")
            .setCancelable(false)
            .setNegativeButton("拒绝") { _, _ ->
                finish()
            }
            .setPositiveButton("去设置") { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            .show()
    }

    /**
     * 跳转 Main, 延迟多少毫秒
     */
    private fun actionHome(delayMillis: Long) {
        mFlAdContainer.postDelayed({
            MainActivity.MainAct.action(this)
            finish()
        }, delayMillis)
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
        AdHelperSplashFull.showAdFull(this, splashConfigAd, mFlAdContainer,
            object : AdHelperSplashFull.AdListenerSplashFull {
                override fun onStartRequest(channel: String?) {
                    Log.e("ifmvo", "onStartRequest$channel")
                }

                override fun onAdClick(channel: String?) {
                    Log.e("ifmvo", channel)
                }

                override fun onAdFailed(failedMsg: String?) {
                    Log.e("ifmvo", "onAdFailed:$failedMsg")
                    actionHome(2000)
                }

                override fun onAdDismissed() {
                    if (canJumpImmediately) {
                        actionHome(0)
                    }
                    canJumpImmediately = true
                }

                override fun onAdPrepared(channel: String?) {
                    Log.e("ifmvo", "onAdPrepared$channel")
                }
            })
    }
}