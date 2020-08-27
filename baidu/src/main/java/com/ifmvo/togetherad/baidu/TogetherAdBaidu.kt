package com.ifmvo.togetherad.baidu

import android.content.Context
import org.jetbrains.annotations.NotNull
import com.baidu.mobads.AdView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity

/**
 * 初始化
 *
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdBaidu {

    var idMapBaidu = mapOf<String, String>()

    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String) {
        init(context, adProviderType, baiduAdAppId, null)
    }

    //baidu
    fun init(@NotNull context: Context, @NotNull adProviderType: String, @NotNull baiduAdAppId: String, baiduIdMap: Map<String, String>? = null) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, BaiduProvider::class.java.name))
        baiduIdMap?.let { idMapBaidu = it }
        AdView.setAppSid(context, baiduAdAppId)
    }
}