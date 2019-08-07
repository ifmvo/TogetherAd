package com.rumtel.ad.other

import android.content.Context
import com.rumtel.ad.TogetherAd

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

    /**
     * configStr : "baidu:3,gdt:7,csj:7"
     *
     * return AdNameType.BAIDU  || AdNameType.GDT || ...
     */
    fun getRandomAdName(configStr: String?): AdNameType {

        logd("广告的配置：$configStr")
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
                            when (keyStr) {
                                AdNameType.BAIDU.type -> {
                                    list.add(AdNameType.BAIDU)
                                }
                                AdNameType.GDT.type -> {
                                    list.add(AdNameType.GDT)
                                }
                                AdNameType.CSJ.type -> {
                                    list.add(AdNameType.CSJ)
                                }
                                else -> {
                                    //如果后台人员拼写字符串出错，忽略即可
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
        logd("随机到的广告: ${adNameType.type}")

        //没有权限就重新随机
        if (adNameType == AdNameType.GDT && !haveGDTNeedPermission(TogetherAd.mContext)) {
            loge("但是没有 GDT 的必要权限: $permissionPhoneStateName 和 $permissionWriteEternalStorageName")
            val newListConfig = configStr.replace(AdNameType.GDT.type, AdNameType.NO.type)
            return getRandomAdName(newListConfig)
        }

        return adNameType
    }

    private fun getRandomInt(max: Int): Int {
        return (1 + Math.random() * (max - 1 + 1)).toInt()
    }

    private val permissionPhoneStateName = "android.permission.READ_PHONE_STATE"
    private val permissionWriteEternalStorageName = "android.permission.WRITE_EXTERNAL_STORAGE"

    /**
     * 有没有 GDT 需要的权限
     */
    private fun haveGDTNeedPermission(context: Context): Boolean {
        return Utils.hasPermission(context, permissionPhoneStateName) &&
                Utils.hasPermission(context, permissionWriteEternalStorageName)
    }
}