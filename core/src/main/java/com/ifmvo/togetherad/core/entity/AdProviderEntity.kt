package com.ifmvo.togetherad.core.entity

import org.jetbrains.annotations.NotNull

/* 
 * 广告提供者（ 广告厂商：百度、广点通、穿山甲... ）
 * 
 * Created by Matthew Chen on 2020-04-03.
 */
data class AdProviderEntity(

        // key 标示，唯一
        @NotNull val providerType: String,

        //Provider 类的路径
        @NotNull val classPath: String,

        //Provider 的描述
        @NotNull val desc: String = classPath

) {
    override fun toString(): String {
        return "AdProviderEntity(providerType=$providerType, classPath='$classPath', desc='$desc')"
    }
}