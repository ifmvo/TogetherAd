package com.ifmvo.togetherad.demo.native_.template

import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2Template
import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2View
import com.ifmvo.togetherad.csj.native_.express.NativeExpress2ViewCsj
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.gdt.native_.express2.NativeExpress2ViewGdt

/**
 *
 * Created by Matthew Chen on 2020/11/27.
 */
class NativeExpress2TemplateSimple : BaseNativeExpress2Template() {

    override fun getNativeExpress2View(adProviderType: String): BaseNativeExpress2View? {
        return when (adProviderType) {
            AdProviderType.CSJ.type -> {
                NativeExpress2ViewCsj()
            }
            AdProviderType.GDT.type -> {
                NativeExpress2ViewGdt()
            }
            else -> {
                throw Exception("模板配置错误")
            }
        }
    }
}