package com.ifmvo.togetherad.demo.native_.template

import com.ifmvo.togetherad.core.custom.express.BaseNativeExpressTemplate
import com.ifmvo.togetherad.core.custom.express.BaseNativeExpressView
import com.ifmvo.togetherad.demo.AdProviderType
import com.ifmvo.togetherad.gdt.native_.express.NativeExpressViewGdt

/**
 *
 * Created by Matthew Chen on 2020/11/27.
 */
class NativeExpressTemplateSimple : BaseNativeExpressTemplate() {

    override fun getNativeExpressView(adProviderType: String): BaseNativeExpressView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeExpressViewGdt()
            }
            else -> {
                throw Exception("模板配置错误")
            }
        }
    }
}