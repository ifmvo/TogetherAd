package com.ifmvo.togetherad.demo.native_

import com.ifmvo.togetherad.baidu.NativeViewBaiduSimple5
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.csj.NativeViewCsjSimple5
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.NativeViewGdtSimple5

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple5 : BaseNativeTemplate() {

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple5()
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple5()
            }
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple5()
            }
            else -> throw Exception("模板配置错误")
        }
    }
}