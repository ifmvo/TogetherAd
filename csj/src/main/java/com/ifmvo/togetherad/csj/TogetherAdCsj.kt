package com.ifmvo.togetherad.csj

import android.content.Context
import androidx.annotation.NonNull
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
        private set

    //照顾 Java 的同学
    fun init(@NonNull context: Context, @NonNull adProviderType: String, @NonNull csjAdAppId: String, @NonNull appName: String, @NonNull csjIdMap: Map<String, String>) {
        init(context, adProviderType, csjAdAppId, appName, csjIdMap, null)
    }

    //穿山甲
    fun init(@NonNull context: Context, @NonNull adProviderType: String, @NonNull csjAdAppId: String, @NonNull appName: String, @NonNull csjIdMap: Map<String, String>, ttAdConfig: TTAdConfig? = null) {

        TogetherAd.addProvider(AdProviderEntity(adProviderType, CsjProvider::class.java.name))

        idMapCsj = csjIdMap
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        val defaultTTAdConfig = TTAdConfig.Builder()
                .appId(csjAdAppId)
                .appName(appName)
                .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(false) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build()

        TTAdSdk.init(context, ttAdConfig ?: defaultTTAdConfig)
    }
}