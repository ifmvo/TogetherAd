package com.ifmvo.togetherad.core.utils

import org.jetbrains.annotations.NotNull


/**
 * 参数 configStr : "baidu:1,gdt:4,csj:4"
 *
 * 按照 2 ：8 的比例随机返回 BAIDU or GDT or CSJ
 *
 * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
 *
 * Created by Matthew_Chen on 2018/8/24.
 */
object AdRandomUtil {

    /**
     * ratioMap: mapOf("baidu" to 3, "gdt" to 7, "csj" to 7)
     */
    fun getRandomAdProvider(@NotNull ratioMap: Map<String, Int>): String? {

        val ratio = StringBuilder()
        val list = mutableListOf<String>()

        ratioMap.entries.forEach { entry ->
            ratio.append("${entry.key}:${entry.value}")
            ratio.append(",")
            repeat(entry.value) {
                list.add(entry.key)
            }
        }

        "提供商比例：$ratio".logi()

        //没有匹配的
        if (list.isEmpty()) return null
        //在list里面随机选择一个
        val adNameType = list[(0 until list.size).random()]
        "随机到的广告: $adNameType".logi()
        return adNameType
    }

    /**
     * ratio : "baidu:3,gdt:7,csj:7"
     * return AdNameType.BAIDU  || AdNameType.GDT || AdNameType.CSJ
     */
    fun getRandomAdProvider(@NotNull ratio: String): String? {
        "广告提供商的比例：$ratio".logi()
        val list = mutableListOf<String>()
        //{baidu:2},{gdt:8}
        val split = ratio.split(",")
        for (itemStr in split) {
            //不能为空
            if (itemStr.isEmpty()) break
            val splitKeyValue = itemStr.split(":")
            //必须分割两份才正确
            if (splitKeyValue.size != 2) break
            //"baidu:2"
            val keyStr = splitKeyValue[0];
            val valueStr = splitKeyValue[1]
            //都不能为空
            if (keyStr.isEmpty() || valueStr.isEmpty()) break
            //加到 list 里面 2 个 "baidu"
            repeat(valueStr.toIntOrNull() ?: 0) {
                list.add(keyStr)
            }
        }
        //没有匹配的
        if (list.size == 0) return null
        //在list里面随机选择一个
        val adNameType = list[(0 until list.size).random()]
        "随机到的广告: $adNameType".logi()
        return adNameType
    }
}


/**
 * 测试工具
 */
//fun main() {
//
//    var a = 0
//    var b = 0
//    var c = 0
//    var d = 0
//
//    val ratioMap = mapOf("a" to 1, "b" to 1, "c" to 1, "d" to 1)
//
//    val startTime = System.currentTimeMillis()
//    repeat(300000) {
//        when (AdRandomUtil.getRandomAdProvider(ratioMap)) {
//            "a" -> a++
//            "b" -> b++
//            "c" -> c++
//            "d" -> d++
//        }
//    }
//    val endTime = System.currentTimeMillis()
//    //main函数执行的代码不能打log，要把log删除
//    println("a: $a, b: $b, c: $c, a: $d, 耗时: ${endTime - startTime}")
//}