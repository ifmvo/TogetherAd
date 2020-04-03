package com.ifmvo.togetherad.core.entity

import androidx.annotation.NonNull
import com.ifmvo.togetherad.core._enum.AdProviderType

/* 
 * 广告提供者（ 广告厂商：百度、广点通、穿山甲... ）
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
data class AdProviderEntity(

        // key 标示，唯一
        @NonNull val providerType: AdProviderType,

        //Provider 类的路径
        @NonNull val classPath: String,

        //Provider 的描述
        @NonNull val desc: String = classPath

) {
    override fun toString(): String {
        return "AdProviderEntity(providerType=$providerType, classPath='$classPath', desc='$desc')"
    }
}