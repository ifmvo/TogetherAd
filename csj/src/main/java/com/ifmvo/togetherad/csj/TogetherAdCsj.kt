package com.ifmvo.togetherad.csj

import android.content.Context
import com.bytedance.sdk.adnet.face.IHttpStack
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTCustomController
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import org.jetbrains.annotations.NotNull

/**
 * 初始化穿山甲
 *
 * Created by Matthew Chen on 2020-04-17.
 */
object TogetherAdCsj {

    // 必须设置，穿山甲的广告位ID
    var idMapCsj = mapOf<String, String>()

    // 可选参数，需在初始化之前，设置是否使用texture播放视频：true使用、false不使用。默认为false不使用（使用的是surface）
    var useTextureView: Boolean = false

    // 可选参数，需在初始化之前，设置落地页主题，默认为TTAdConstant#TITLE_BAR_THEME_LIGHT
    var titleBarTheme: Int = TTAdConstant.TITLE_BAR_THEME_LIGHT

    // 可选参数，需在初始化之前，设置是否允许SDK弹出通知：true允许、false禁止。默认为true允许
    var allowShowNotify: Boolean = true

    // 可选参数，需在初始化之前，设置是否允许落地页出现在锁屏上面：true允许、false禁止。默认为false禁止
    @Deprecated(message = "被穿山甲Deprecated了")
    var allowShowPageWhenScreenLock = true

    // 可选参数，需在初始化之前，是否打开debug调试信息输出：true打开、false关闭。默认false关闭
    var debug: Boolean = false

    // 可选参数，需在初始化之前，哪些网络情况下允许直接下载
    var directDownloadNetworkType = TTAdConstant.NETWORK_STATE_WIFI or TTAdConstant.NETWORK_STATE_4G

    // 可选参数，需在初始化之前，设置是否支持多进程：true支持、false不支持。默认为false不支持
    var supportMultiProcess: Boolean = false

    // 可选参数，需在初始化之前，设置外部网络请求，默认为 urlconnection
    var httpStack: IHttpStack? = null

    // 可选参数，需在初始化之前，设置是否为计费用户：true计费用户、false非计费用户。默认为false非计费用户。须征得用户同意才可传入该参数
    var isPaid: Boolean = false

    // 可选参数，需在初始化之前，设置用户画像的关键词列表 **不能超过为1000个字符**。须征得用户同意才可传入该参数
    var keywords: String? = null

    // 可选参数，需在初始化之前，设置额外的用户信息 **不能超过为1000个字符**
    var data: String? = null

    // 可选参数，需在初始化之前，是否一步初始化
    var isAsyncInit: Boolean = false

    //可选参数，需在初始化之前，可以设置隐私信息控制开关
    var customController: TTCustomController? = null

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
        ttAdConfig.paid(isPaid)
        ttAdConfig.asyncInit(isAsyncInit)
        keywords?.let { ttAdConfig.keywords(it) }
        data?.let { ttAdConfig.data(it) }
        httpStack?.let { ttAdConfig.httpStack(it) } //自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
        customController?.let { ttAdConfig.customController(it) }
        TTAdSdk.init(context, ttAdConfig.build())
    }
}