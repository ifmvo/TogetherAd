package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.ifmvo.togetherad.core.helper.AdHelperNative
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.TogetherAdAlias
import kotlinx.android.synthetic.main.activity_native_recyclerview.*


/**
 * 原生自渲染在 RecyclerView 中的用法
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeRecyclerViewActivity : AppCompatActivity() {

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeRecyclerViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_recyclerview)

        val contentData = getContentData()

        requestAd {

            //使用 RecyclerView 展示合并后的数据
            val allList = mergeContentAd(contentData, it)
            rv.layoutManager = LinearLayoutManager(this@NativeRecyclerViewActivity)
            rv.adapter = NativeAdapter(allList, this@NativeRecyclerViewActivity)
        }
    }

    /**
     * 把内容List和广告List合并
     */
    private fun mergeContentAd(contentList: List<ContentDataEntity>, adList: List<Any>): List<Any> {

        var nextAdPosition = 0
        var lastUseAdPosition = 0

        val multiItemList = mutableListOf<Any>()
        repeat(contentList.size) {
            multiItemList.add(contentList[it])
            if (adList.isNotEmpty() && nextAdPosition == it) {
                if (lastUseAdPosition > adList.size - 1) {
                    lastUseAdPosition = 0
                }
                multiItemList.add(adList[lastUseAdPosition])
                lastUseAdPosition += 1
                nextAdPosition += 5
            }
        }
        return multiItemList
    }

    /**
     * 模拟真实的内容数据List
     */
    private fun getContentData(): List<ContentDataEntity> {
        val contentList = mutableListOf<ContentDataEntity>()
        for (index in 1..15) {
            val title = "正文内容序号：$index"
            contentList.add(ContentDataEntity(title = title, imgUrl = "https://t8.baidu.com/it/u=2247852322,986532796&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1590128472&t=657ec840a5c6c658430135ea8b1d35f0"))
        }
        return contentList
    }

    /**
     * 请求广告List
     */
    private fun requestAd(onResult: (adList: List<Any>) -> Unit) {
        AdHelperNative.getList(this@NativeRecyclerViewActivity, TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, maxCount = 3, listener = object : NativeListener {
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
                Toast.makeText(this@NativeRecyclerViewActivity, "所有平台都请求失败了", Toast.LENGTH_LONG).show()
                //所有的提供商都失败
                onResult(mutableListOf())
            }
        })
    }
}

class ContentDataEntity(val title: String, val imgUrl: String)