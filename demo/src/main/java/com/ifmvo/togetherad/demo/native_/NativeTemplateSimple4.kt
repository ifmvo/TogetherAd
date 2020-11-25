package com.ifmvo.togetherad.demo.native_

import com.ifmvo.togetherad.baidu.native_.view.NativeViewBaiduSimple4
import com.ifmvo.togetherad.core.custom.flow.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.flow.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple4
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple4

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple4(onDismiss: (providerType: String) -> Unit) : BaseNativeTemplate() {

    private var mOnDismiss: (providerType: String) -> Unit = onDismiss

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple4(mOnDismiss)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple4(mOnDismiss)
            }
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple4(mOnDismiss)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}