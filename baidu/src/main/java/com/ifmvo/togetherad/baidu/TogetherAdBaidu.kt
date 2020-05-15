package com.ifmvo.togetherad.baidu

import android.app.Application
import androidx.annotation.NonNull
import com.baidu.mobads.AdView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.entity.AdProviderEntity

/* 
 *
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdBaidu {

    var idMapBaidu = mapOf<String, String>()
        private set

    /**
     * 初始化广告
     */
    //baidu
    fun init(@NonNull context: Application, @NonNull adProviderType: String, @NonNull baiduAdAppId: String, baiduIdMap: Map<String, String>) {
        TogetherAd.addProvider(AdProviderEntity(adProviderType, BaiduProvider::class.java.name))
        AdView.setAppSid(context, baiduAdAppId)
        idMapBaidu = baiduIdMap
    }
}