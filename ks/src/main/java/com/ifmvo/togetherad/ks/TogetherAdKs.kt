package com.ifmvo.togetherad.ks

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.ks.provider.KsProvider
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsLoadManager
import com.kwad.sdk.api.SdkConfig
import org.jetbrains.annotations.NotNull

object TogetherAdKs {

    var idMapKs = mutableMapOf<String, Long>()

    // 可选参数，需在初始化之前，是否打开debug调试信息输出：true打开、false关闭。默认false关闭
    var debug: Boolean = false

    //直播sdk安全验证，接入直播模块必填
    var appKey: String? = null

    // 直播sdk安全验证，接入直播模块必填
    var appWebKey: String? = null

    // Feed和入口组件，夜间模式样式配置，如果不配置 默认是"ks_adsdk_night_styles.xml"
    var nightThemeStyleAssetsFileName: String? = null

    // 是否展示下载通知栏
    var showNotification: Boolean = false

    //全局请求广告
    var adRequestManager: KsLoadManager? = null

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull ksAdAppId: String): Boolean {
        return init(context, adProviderType, ksAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull ksAdAppId: String, providerClassPath: String? = null): Boolean {
        return init(context, adProviderType, ksAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull ksAdAppId: String, ksIdMap: Map<String, Long>? = null): Boolean {
        return init(context, adProviderType, ksAdAppId, ksIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull ksAdAppId: String, ksIdMap: Map<String, Long>? = null, providerClassPath: String? = null): Boolean {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) KsProvider::class.java.name else providerClassPath))
        ksIdMap?.let { idMapKs.putAll(it) }
        val config = SdkConfig.Builder()
        config.appId(ksAdAppId)
        appKey?.let { config.appKey(it) }
        appWebKey?.let { config.appWebKey(it) }
        nightThemeStyleAssetsFileName?.let { config.nightThemeStyleAssetsFileName(it) }
        config.showNotification(showNotification)
        config.debug(debug)
        val isInitSuccess = KsAdSDK.init(context, config.build())
        adRequestManager = KsAdSDK.getLoadManager()
        return isInitSuccess
    }

}