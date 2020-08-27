//package com.ifmvo.togetherad.core.utils
//
//import java.lang.reflect.InvocationHandler
//import java.lang.reflect.Method
//
//
///**
// *
// * Created by Matthew Chen on 2020/8/27.
// */
//class ProxyHandler(proxy: Any?) : InvocationHandler {
//
//    private var mProxy = proxy
//
//    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any? {
//        method?.invoke(mProxy, args)
//        return null
//    }
//}