package com.ifmvo.togetherad.demo.flow

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ifmvo.quicklist.BaseRecyclerViewFragment
import com.ifmvo.togetherad.demo.R

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew Chen on 2020-04-21.
 */
class FlowRecyclerViewFragment : BaseRecyclerViewFragment<FlowRVMultiItemEntity, BaseViewHolder>() {

    override fun getData(currentPage: Int) {


    }

    override fun initRecyclerViewAdapter() {
        mAdapter = object : BaseMultiItemQuickAdapter<FlowRVMultiItemEntity, BaseViewHolder>(), LoadMoreModule {

            init {
                addItemType(FlowRVMultiItemEntity.TYPE_CONTENT, R.layout.list_item_flow_content)
                addItemType(FlowRVMultiItemEntity.TYPE_AD, R.layout.list_item_flow_content)
            }

            override fun convert(holder: BaseViewHolder, item: FlowRVMultiItemEntity) {

            }
        }
    }
}