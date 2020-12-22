package com.ifmvo.togetherad.baidu

import android.content.Context
import org.jetbrains.annotations.NotNull
import com.baidu.mobads.AdView
import com.ifmvo.togetherad.baidu.provider.BaiduProvider
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity

/**
 * 初始化百青藤
 *
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdBaidu {

    var idMapBaidu = mutableMapOf<String, String>()

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String) {
        init(context, adProviderType, baiduAdAppId, null, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String, providerClassPath: String? = null) {
        init(context, adProviderType, baiduAdAppId, null, providerClassPath)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String, baiduIdMap: Map<String, String>? = null) {
        init(context, adProviderType, baiduAdAppId, baiduIdMap, null)
    }

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String, baiduIdMap: Map<String, String>? = null, providerClassPath: String? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, if (providerClassPath?.isEmpty() != false) BaiduProvider::class.java.name else providerClassPath))
        baiduIdMap?.let { idMapBaidu.putAll(it) }
        AdView.setAppSid(context, baiduAdAppId)
    }
}