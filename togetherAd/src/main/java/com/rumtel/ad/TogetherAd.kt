package com.rumtel.ad

import android.content.Context
import android.support.annotation.NonNull
import com.baidu.mobads.AdView
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/12/26.
 */
object TogetherAd {

    /**
     * 位ID的Map
     */
    var idMapBaidu = mutableMapOf<String, String>()
        private set
    var idMapGDT = mutableMapOf<String, String>()
        private set
    var idMapCsj = mutableMapOf<String, String>()
        private set

    /**
     * 广点通的 AppId
     */
    var appIdGDT = ""
        private set

    /**
     * 超时时间
     */
    var timeOutMillis: Long = 5000
        private set

    /**
     * 前贴
     */
    var preMoivePaddingSize = 0
        private set

    /**
     * 初始化广告
     */
    //baidu
    fun initBaiduAd(@NonNull context: Context, @NonNull baiduAdAppId: String, baiduIdMap: MutableMap<String, String>) {
        AdView.setAppSid(context, baiduAdAppId)
        idMapBaidu = baiduIdMap
    }

    //广点通
    fun initGDTAd(@NonNull context: Context, @NonNull gdtAdAppId: String, @NonNull gdtIdMap: MutableMap<String, String>) {
        idMapGDT = gdtIdMap
        appIdGDT = gdtAdAppId
    }

    //穿山甲
    fun initCsjAd(@NonNull context: Context, @NonNull csjAdAppId: String, @NonNull appName: String, @NonNull csjIdMap: MutableMap<String, String>) {
        idMapCsj = csjIdMap
        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        TTAdSdk.init(
            context, TTAdConfig.Builder()
                .appId(csjAdAppId)
                .appName(appName)
                .useTextureView(false) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
                .titleBarTheme(TTAdConstant.TITLE_BAR_THEME_DARK)
                .allowShowNotify(true) //是否允许sdk展示通知栏提示
                .allowShowPageWhenScreenLock(true) //是否在锁屏场景支持展示广告落地页
                .debug(BuildConfig.DEBUG) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
                .directDownloadNetworkType(
                    TTAdConstant.NETWORK_STATE_WIFI
                ) //允许直接下载的网络状态集合
                .supportMultiProcess(false) //是否支持多进程，true支持
                //.httpStack(new MyOkStack3())//自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
                .build()
        )
    }

    fun setAdTimeOutMillis(millis: Long) {
        timeOutMillis = millis
    }

    fun setPreMoiveMarginTopSize(height: Int) {
        preMoivePaddingSize = height
    }


}