package com.ifmvo.togetherad.core.custom.express2

import com.ifmvo.togetherad.core.custom.express2.BaseNativeExpress2View

/**
 *
 * Created by Matthew Chen on 2020/11/27.
 */
abstract class BaseNativeExpress2Template {

    abstract fun getNativeExpress2View(adProviderType: String): BaseNativeExpress2View?

}