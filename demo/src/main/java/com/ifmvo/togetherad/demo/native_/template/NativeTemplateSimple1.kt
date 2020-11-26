package com.ifmvo.togetherad.demo.native_.template

import com.ifmvo.togetherad.baidu.native_.view.NativeViewBaiduSimple1
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple1
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple1

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple1 : BaseNativeTemplate() {

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple1()
            }
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple1()
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple1()
            }
            else -> throw Exception("模板配置错误")
        }
    }
}