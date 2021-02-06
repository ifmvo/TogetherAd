package com.ifmvo.togetherad.csj

import android.content.Context
import com.bytedance.sdk.adnet.face.IHttpStack
import com.bytedance.sdk.openadsdk.TTAdConfig
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTAdSdk
import com.bytedance.sdk.openadsdk.TTCustomController
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.csj.provider.CsjProvider
import org.jetbrains.annotations.NotNull

/**
 * 初始化穿山甲
 *
 * Created by Matthew Chen on 2020-04-17.
 */
object TogetherAdCsj {

    // 必须设置，穿山甲的广告位ID
    var idMapCsj = mutableMapOf<String, String>()

    // 可选参数，需在初始化之前，设置是否使用texture播放视频：true使用、false不使用。默认为false不使用（使用的是surface）
    var useTextureView: Boolean = false

    // 可选参数，需在初始化之前，设置落地页主题，默认为TTAdConstant#TITLE_BAR_THEME_LIGHT
    var titleBarTheme: Int = TTAdConstant.TITLE_BAR_THEME_LIGHT

    // 可选参数，需在初始化之前，设置是否允许SDK弹出通知：true允许、false禁止。默认为true允许
    var allowShowNotify: Boolean = true

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

    // 可选参数，需在初始化之前，是否异步初始化
    @Deprecated(message = "穿山甲改变了异步初始化的方式, 使用 initCallback 替换", replaceWith = ReplaceWith(expression = "initCallback", imports = ["com.ifmvo.togetherad.csj"]), level = DeprecationLevel.WARNING)
    var isAsyncInit: Boolean = false

    // 可选参数，需在初始化之前，可以设置隐私信息控制开关
    var customController: TTCustomController? = null

    // 可选参数，异步初始化回调
    var initCallback: TTAdSdk.InitCallback? = null

    // 判断穿山甲SDK是否初始化成功
    var isInitSuccess = TTAdSdk.isInitSuccess()

    /**
     * 简单初始化
     */
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String) {
        init(context, adProviderType, csjAdAppId, appName, null, null)
    }

    /**
     * 自定义Provider初始化
     */
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String, providerClassPath: String? = null) {
        init(context, adProviderType, csjAdAppId, appName, null, providerClassPath)
    }

    /**
     * 广告位ID 初始化
     */
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String, csjIdMap: Map<String, String>? = null) {
        init(context, adProviderType, csjAdAppId, appName, csjIdMap, null)
    }

    /**
     * 自定义Provider + 广告位ID 一起初始化
     */
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull csjAdAppId: String, @NotNull appName: String, csjIdMap: Map<String, String>? = null, providerClassPath: String?) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) CsjProvider::class.java.name else providerClassPath))

        csjIdMap?.let { idMapCsj.putAll(it) }

        //强烈建议在应用对应的Application#onCreate()方法中调用，避免出现content为null的异常
        val ttAdConfig = TTAdConfig.Builder()
        ttAdConfig.appId(csjAdAppId)
        ttAdConfig.appName(appName)
        ttAdConfig.useTextureView(useTextureView) //使用TextureView控件播放视频,默认为SurfaceView,当有SurfaceView冲突的场景，可以使用TextureView
        ttAdConfig.titleBarTheme(titleBarTheme)
        ttAdConfig.allowShowNotify(allowShowNotify) //是否允许sdk展示通知栏提示
        ttAdConfig.debug(debug) //测试阶段打开，可以通过日志排查问题，上线时去除该调用
        ttAdConfig.directDownloadNetworkType(directDownloadNetworkType) //允许直接下载的网络状态集合
        ttAdConfig.supportMultiProcess(supportMultiProcess) //是否支持多进程，true支持
        ttAdConfig.paid(isPaid)
        ttAdConfig.asyncInit(isAsyncInit)
        keywords?.let { ttAdConfig.keywords(it) }
        data?.let { ttAdConfig.data(it) }
        httpStack?.let { ttAdConfig.httpStack(it) } //自定义网络库，demo中给出了okhttp3版本的样例，其余请自行开发或者咨询工作人员。
        customController?.let { ttAdConfig.customController(it) }
        //初始化
        if (initCallback == null) {
            TTAdSdk.init(context, ttAdConfig.build())
        } else {
            TTAdSdk.init(context, ttAdConfig.build(), initCallback)
        }
    }
}