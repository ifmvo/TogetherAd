package com.ifmvo.togetherad.core

/**
 * 分发类型
 *
 * Created by Matthew Chen on 2020/12/3.
 */
enum class DispatchType {

    /**
     * 按照权重随机分发
     */
    Random,

    /**
     * 按照优先级分发广告
     */
    Priority

}