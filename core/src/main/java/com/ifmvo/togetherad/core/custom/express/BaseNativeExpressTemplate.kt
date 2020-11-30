package com.ifmvo.togetherad.core.custom.express

/**
 *
 * Created by Matthew Chen on 2020/11/27.
 */
abstract class BaseNativeExpressTemplate {

    abstract fun getNativeExpressView(adProviderType: String): BaseNativeExpressView?

}