package com.ifmvo.togetherad.demo.native_

import com.ifmvo.togetherad.baidu.NativeViewBaiduSimple3
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.csj.NativeViewCsjSimple3
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.NativeViewGdtSimple3

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple3(onDismiss: (providerType: String) -> Unit) : BaseNativeTemplate() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple3(mOnDismiss)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple3(mOnDismiss)
            }
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple3(mOnDismiss)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}