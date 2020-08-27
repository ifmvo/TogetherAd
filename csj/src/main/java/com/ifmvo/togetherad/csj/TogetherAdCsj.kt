package com.ifmvo.togetherad.csj

import android.content.Context
import org.jetbrains.annotations.NotNull
import com.bytedance.sdk.adnet.face.IHttpStack
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity

/**
 * 初始化穿山甲
 *
 * Created by Matthew Chen on 2020-04-17.
 */
object TogetherAdCsj {

    var idMapCsj = mapOf<String, String>()

    var useTextureView = false

    var titleBarTheme = TTAdConstant.TITLE_BAR_THEME_DARK

    var allowShowNotify = true

    var allowShowPageWhenScreenLock = true

    var debug = false

    var directDownloadNetworkType = TTAdConstant.NETWORK_STATE_WIFI or TTAdConstant.NETWORK_STATE_4G

    var supportMultiProcess = false

    var httpStack: IHttpStack? = null

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String) {
        init(context, adProviderType, csjAdAppId, appName, null)
    }

    //穿山甲
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String, csjIdMap: Map<String, String>? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, CsjProvider::class.java.name))

        csjIdMap?.let { idMapCsj = it }

        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        val ttAdConfig = TTAdConfig.Builder()
        ttAdConfig.appId(csjAdAppId)
        ttAdConfig.appName(appName)
        ttAdConfig.useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
        ttAdConfig.titleBarTheme(titleBarTheme)
        ttAdConfig.allowShowNotify(allowShowNotify) //是否允许sdk展示通知栏提示
        ttAdConfig.allowShowPageWhenScreenLock(allowShowPageWhenScreenLock) //是否在锁屏场景支持展示广告落地页
        ttAdConfig.debug(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
        ttAdConfig.directDownloadNetworkType(directDownloadNetworkType) //允许直接下载的网络状态集合
        ttAdConfig.supportMultiProcess(supportMultiProcess) //是否支持多进程，true支持
        httpStack?.let { ttAdConfig.httpStack(it) } //自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
        TTAdSdk.init(context, ttAdConfig.build())
    }
}