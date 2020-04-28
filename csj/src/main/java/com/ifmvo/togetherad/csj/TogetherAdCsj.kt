package com.ifmvo.togetherad.csj

import android.app.Application
import androidx.annotation.NonNull
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-17.
 */
object TogetherAdCsj {

    var idMapCsj = mapOf<String, String>()
        private set

    //穿山甲
    fun init(@NonNull context: Application, @NonNull adProviderType: String, @NonNull csjAdAppId: String, @NonNull appName: String, @NonNull csjIdMap: Map<String, String>, useTextureView: Boolean = false, isDebug: Boolean = false) {

        TogetherAd.addProvider(AdProviderEntity(adProviderType, CsjProvider::class.java.name))

        idMapCsj = csjIdMap
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(context, TTAdConfig.Builder()
                .appId(csjAdAppId)
                .appName(appName)
                .useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(isDebug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(TTAdConstant.NETWORK_STATE_WIFI) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build()
        )
        "初始化$adProviderType".logi()
    }

}