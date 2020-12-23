package com.ifmvo.togetherad.demo.express2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.ifmvo.togetherad.core.helper.AdHelperNativeExpress2
import com.ifmvo.togetherad.core.listener.NativeExpress2Listener
import com.ifmvo.togetherad.demo.app.AdProviderType
import com.ifmvo.togetherad.demo.R
import com.ifmvo.togetherad.demo.app.TogetherAdAlias
import com.ifmvo.togetherad.demo.other.ContentDataEntity
import kotlinx.android.synthetic.main.activity_native_recyclerview.*


/**
 * 原生自渲染在 RecyclerView 中的用法
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class NativeExpress2RecyclerViewActivity : AppCompatActivity() {

    //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
    private val ratioMapNativeExpress2Recycler = mapOf(
            AdProviderType.GDT.type to 1,
            AdProviderType.CSJ.type to 1
    )

    private val adHelperNativeExpress2Rv by lazy { AdHelperNativeExpress2(this, TogetherAdAlias.AD_NATIVE_EXPRESS_2_RECYCLERVIEW, /*ratioMapNativeRecycler,*/ 3) }

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeExpress2RecyclerViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_recyclerview)

        requestRvAd {
            //使用 RecyclerView 展示合并后的数据
            val allList = mergeContentAd(getContentData(), it)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = NativeExpress2Adapter(this, allList)
        }
    }

    /**
     * 请求广告List
     */
    private fun requestRvAd(onResult: (adList: List<Any>) -> Unit) {
        adHelperNativeExpress2Rv.getExpress2List(listener = object : NativeExpress2Listener {
            override fun onAdStartRequest(providerType: String) {
                //每个提供商请求之前都会回调
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                onResult(adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                //单个提供商请求失败
            }

            override fun onAdFailedAll(failedMsg: String?) {
                //所有的提供商都失败
                onResult(mutableListOf())
            }
        })
    }

    /**
     * 把内容List和广告List合并
     *
     * 示例：第二条插入广告，之后每隔5条内容插入一条广告
     *
     * 具体逻辑按照自己的需求自行处理
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

    override fun onDestroy() {
        super.onDestroy()
        adHelperNativeExpress2Rv.destroyAllExpress2Ad()
    }
}