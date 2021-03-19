package com.ifmvo.togetherad.demo.native_.template

import com.ifmvo.togetherad.baidu.native_.view.NativeViewBaiduSimple4
import com.ifmvo.togetherad.core.custom.native_.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.native_.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple4
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple4

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple4(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeTemplate() {

    private var mOnClose = onClose

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple4(mOnClose)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple4(mOnClose)
            }
            AdProviderType.BAIDU.type -> {
                NativeViewBaiduSimple4(mOnClose)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}