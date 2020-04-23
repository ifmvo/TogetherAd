package com.ifmvo.togetherad.demo.native_

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
class NativeRecyclerViewFragment : BaseRecyclerViewFragment<NativeRVMultiItemEntity, BaseViewHolder>() {

    override fun getData(currentPage: Int) {


    }

    override fun initRecyclerViewAdapter() {
        mAdapter = object : BaseMultiItemQuickAdapter<NativeRVMultiItemEntity, BaseViewHolder>(), LoadMoreModule {

            init {
                addItemType(NativeRVMultiItemEntity.TYPE_CONTENT, R.layout.list_item_flow_content)
                addItemType(NativeRVMultiItemEntity.TYPE_AD, R.layout.list_item_flow_content)
            }

            override fun convert(holder: BaseViewHolder, item: NativeRVMultiItemEntity) {

            }
        }
    }
}