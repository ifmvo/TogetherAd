package com.ifmvo.togetherad.baidu

import android.app.Application
import androidx.annotation.NonNull
import com.baidu.mobads.AdView
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core._enum.AdProviderType
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.utils.logi

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-04.
 */
object TogetherAdBaidu {

    var idMapBaidu = mutableMapOf<String, String>()
        private set

    /**
     * 初始化广告
     */
    //baidu
    fun init(@NonNull context: Application, @NonNull baiduAdAppId: String, baiduIdMap: MutableMap<String, String>) {

        TogetherAd.addProvider(AdProviderEntity(AdProviderType.BAIDU, BaiduProvider::class.java.name))

        AdView.setAppSid(context, baiduAdAppId)
        idMapBaidu = baiduIdMap
        "初始化${AdProviderType.BAIDU.type}".logi()
    }
}