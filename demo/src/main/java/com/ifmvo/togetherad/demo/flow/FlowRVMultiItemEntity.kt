package com.ifmvo.togetherad.demo.flow

import com.chad.library.adapter.base.entity.MultiItemEntity

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
class FlowRVMultiItemEntity(override val itemType: Int, obj: Any) : MultiItemEntity {

    companion object {
        const val TYPE_CONTENT = 1
        const val TYPE_AD = 2
    }

}