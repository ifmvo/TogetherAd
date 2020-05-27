package com.ifmvo.togetherad.demo.native_

import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.ifmvo.quicklist.BaseRecyclerViewFragment
import com.ifmvo.togetherad.core.helper.AdHelperNative
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.core.listener.NativeViewListener
import com.ifmvo.togetherad.core.utils.ScreenUtil
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias

/**
 * 原生自渲染在 RecyclerView 中的用法
 *
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeRecyclerViewFragment : BaseRecyclerViewFragment<NativeRVMultiItemEntity, BaseViewHolder>() {

    override fun getData(currentPage: Int) {
        val contentList = mutableListOf<ContentDataEntity>()
        for (index in 1..15) {
            val title = "正文内容序号：${((currentPage - 1) * 15) + index}"
            contentList.add(ContentDataEntity(title = title, imgUrl = "https://t8.baidu.com/it/u=2247852322,986532796&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1590128472&t=657ec840a5c6c658430135ea8b1d35f0"))
        }

        requestAd {
            val newList = mergeContentAd(contentList = contentList, adList = it)
            handleListData(newList, currentPage)
        }
    }

    private fun requestAd(onResult: (adList: List<Any>) -> Unit) {
        AdHelperNative.getList(requireActivity(), TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, maxCount = 3, listener = object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                //每个提供商请求之前都会回调
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                onResult(adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //单个提供商请求失败
            }

            override fun onAdFailedAll() {
                Toast.makeText(mContext, "所有平台都请求失败了", Toast.LENGTH_LONG).show()
                //所有的提供商都失败
                onResult(mutableListOf())
            }
        })
    }

    /**
     * 把内容和广告合并
     */
    private fun mergeContentAd(contentList: List<ContentDataEntity>, adList: List<Any>): List<NativeRVMultiItemEntity> {

        var nextAdPosition = 0
        var lastUseAdPosition = 0

        val multiItemList = mutableListOf<NativeRVMultiItemEntity>()
        repeat(contentList.size) {
            multiItemList.add(NativeRVMultiItemEntity(itemType = NativeRVMultiItemEntity.TYPE_CONTENT, anyObj = contentList[it]))
            if (adList.isNotEmpty() && nextAdPosition == it) {
                if (lastUseAdPosition > adList.size - 1) {
                    lastUseAdPosition = 0
                }
                multiItemList.add(NativeRVMultiItemEntity(itemType = NativeRVMultiItemEntity.TYPE_AD, anyObj = adList[lastUseAdPosition]))
                lastUseAdPosition += 1
                nextAdPosition += 5
            }
        }
        return multiItemList
    }

    override fun initRecyclerViewAdapter() {
        mAdapter = object : BaseMultiItemQuickAdapter<NativeRVMultiItemEntity, BaseViewHolder>(), LoadMoreModule {

            init {
                addItemType(NativeRVMultiItemEntity.TYPE_CONTENT, R.layout.list_item_native_content)
                addItemType(NativeRVMultiItemEntity.TYPE_AD, R.layout.list_item_native_ad)
            }

            override fun convert(holder: BaseViewHolder, item: NativeRVMultiItemEntity) {
                when (item.itemType) {
                    NativeRVMultiItemEntity.TYPE_CONTENT -> {
                        val content = item.anyObj as ContentDataEntity
                        holder.setText(R.id.txt, content.title)
                        val imgView = holder.getView<ImageView>(R.id.img)
                        imgView.layoutParams.height = ScreenUtil.getDisplayMetricsWidth(requireContext()) * 9 / 16
                        Glide.with(requireContext()).load(content.imgUrl).into(imgView)
                    }
                    NativeRVMultiItemEntity.TYPE_AD -> {
                        val adContainer = holder.getView<ViewGroup>(R.id.adContainer)
                        adContainer.removeAllViews()
                        AdHelperNative.show(item.anyObj, adContainer, NativeTemplateCommon(), object : NativeViewListener {
                            override fun onAdExposed(providerType: String) {
                                Toast.makeText(mContext, "原生广告曝光了：$providerType", Toast.LENGTH_LONG).show()
                            }

                            override fun onAdClicked(providerType: String) {
                                Toast.makeText(mContext, "原生广告点击了：$providerType", Toast.LENGTH_LONG).show()
                            }
                        })
                    }
                }
            }
        }
    }
}

class ContentDataEntity(val title: String, val imgUrl: String)