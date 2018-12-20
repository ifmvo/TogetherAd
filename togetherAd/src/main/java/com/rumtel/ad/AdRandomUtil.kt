package com.rumtel.ad

import android.util.Log

/*
 * (●ﾟωﾟ●)
 *
 * 参数 configStr : "baidu:2,gdt:8"
 *
 * 按照 2 ：8 的比例随机返回 BAIDU or GDT
 *
 * return AdNameType.BAIDU  || AdNameType.GDT || ...
 *
 * Created by Matthew_Chen on 2018/8/24.
 */
object AdRandomUtil {

    const val tag = "ifmvo"

    /**
     * configStr : "baid:2,gdt:8"
     *
     * return AdNameType.BAIDU  || AdNameType.GDT || ...
     */
    fun getRandomAdName(configStr: String?): AdNameType {

        if (configStr.isNullOrEmpty()) {
            return AdNameType.NO
        }

        val list = ArrayList<AdNameType>()
        //{baidu:2},{gdt:8}
        val split = configStr.split(",")
        repeat(split.size) { it ->
            // baidu:2
            val itemStr = split[it]
            if (itemStr.isNotEmpty()) {
                val splitKeyValue = itemStr.split(":")
                if (splitKeyValue.size == 2) {
                    //baidu
                    val keyStr = splitKeyValue[0]
                    // 2
                    val valueStr = splitKeyValue[1]
                    if (keyStr.isNotEmpty() && valueStr.isNotEmpty()) {
                        //加到 list 里面 2 个 "baidu"
                        repeat(valueStr.toInt()) {
                            when (keyStr.toUpperCase()) {
                                AdNameType.BAIDU.type -> {
                                    list.add(AdNameType.BAIDU)
                                }
                                AdNameType.GDT.type -> {
                                    list.add(AdNameType.GDT)
                                }
                                else -> {
                                }
                            }
                        }
                    }
                }
            }
        }

        if (list.size == 0) {
            return AdNameType.NO
        }

        val adNameType = list[(getRandomInt(list.size)) - 1]
        Log.e(tag, adNameType.type)
        return adNameType
    }

    private fun getRandomInt(max: Int): Int {
        return (1 + Math.random() * (max - 1 + 1)).toInt()
    }
}