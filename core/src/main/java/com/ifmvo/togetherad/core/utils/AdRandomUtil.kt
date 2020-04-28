package com.ifmvo.togetherad.core.utils

import androidx.annotation.NonNull

/*
 * (●ﾟωﾟ●)
 *
 * 参数 configStr : "baidu:1,gdt:4,csj:4"
 *
 * 按照 2 ：8 的比例随机返回 BAIDU or GDT or CSJ
 *
 * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
 *
 * Created by Matthew_Chen on 2018/8/24.
 */
object AdRandomUtil {

    fun getRandomAdProvider(@NonNull radioMap: Map<String, Int>): String? {
        val list = mutableListOf<String>()

        radioMap.entries.forEach { entry ->
            repeat(entry.value) {
                list.add(entry.key)
            }
        }

        //没有匹配的
        if (list.isEmpty()) return null
        //在list里面随机选择一个
        val adNameType = list[(0 until list.size).random()]
        "随机到的广告: $adNameType".logi()
        return adNameType
    }

    //    /**
    //     * radio : "baidu:3,gdt:7,csj:7"
    //     * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
    //     */
    //    fun getRandomAdProvider(@NonNull radio: String): String {
    //        "广告提供商的比例：$radio".logi()
    //        val list = mutableListOf<String>()
    //        //{baidu:2},{gdt:8}
    //        val split = radio.split(",")
    //        for (itemStr in split) {
    //            //不能为空
    //            if (itemStr.isEmpty()) break
    //            val splitKeyValue = itemStr.split(":")
    //            //必须分割两份才正确
    //            if (splitKeyValue.size != 2) break
    //            //"baidu:2"
    //            val keyStr = splitKeyValue[0];
    //            val valueStr = splitKeyValue[1]
    //            //都不能为空
    //            if (keyStr.isEmpty() || valueStr.isEmpty()) break
    //            //加到 list 里面 2 个 "baidu"
    //            repeat(valueStr.toIntOrNull() ?: 0) {
    //                list.add(keyStr)
    //            }
    //        }
    //        //没有匹配的
    //        if (list.size == 0) return NO
    //        //在list里面随机选择一个
    //        val adNameType = list[(0 until list.size).random()]
    //        "随机到的广告: $adNameType".logi()
    //        return adNameType
    //    }
}


///**
// * 测试工具
// */
//fun main() {
//
//    var baidu = 0
//    var gdt = 0
//    var csj = 0
//
//    val startTime = System.currentTimeMillis()
//    repeat(3000000) {
//        when (AdRandomUtil.getRandomAdProvider("baidu:10,gdt:10,csj:10").type) {
//            AdProviderType.BAIDU.type -> baidu++
//            AdProviderType.GDT.type -> gdt++
//            AdProviderType.CSJ.type -> csj++
//        }
//    }
//    val endTime = System.currentTimeMillis()
//    //main函数执行的代码不能打log，要把log删除
//    println("baidu: $baidu, gdt: $gdt, csj: $csj, 耗时: ${endTime - startTime}")
//}