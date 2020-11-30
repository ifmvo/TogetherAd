package com.ifmvo.togetherad.demo.native_

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.bytedance.sdk.openadsdk.AdSlot
import com.ifmvo.togetherad.core.helper.AdHelperNativePro
import com.ifmvo.togetherad.core.listener.NativeListener
import com.ifmvo.togetherad.csj.provider.CsjProvider
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
class NativeRecyclerViewActivity : AppCompatActivity() {

    //使用 Map<String, Int> 配置广告商 权重，通俗的讲就是 随机请求的概率占比
    private val ratioMapNativeRecycler = mapOf(
            AdProviderType.GDT.type to 1,
            AdProviderType.CSJ.type to 1,
            AdProviderType.BAIDU.type to 1
    )

    private val adHelperNativeRv by lazy { AdHelperNativePro(this, TogetherAdAlias.AD_NATIVE_RECYCLERVIEW, /*ratioMapNativeRecycler,*/ 3) }

    companion object {
        fun action(context: Context) {
            context.startActivity(Intent(context, NativeRecyclerViewActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_native_recyclerview)

        requestRvAd {
            //使用 RecyclerView 展示合并后的数据
            val allList = mergeContentAd(getContentData(), it)
            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = NativeAdapter(allList)
        }
    }

    /**
     * 请求广告List
     */
    private fun requestRvAd(onResult: (adList: List<Any>) -> Unit) {
        //--------------------------------------------------------------------------------------
        //  （ 必须设置 ）如果需要使用穿山甲的原生广告，必须在请求之前设置类型。（ 没用到穿山甲的请忽略 ）
        //  设置方式：CsjProvider.Native.nativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_INTERACTION_AD
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_BANNER
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_CACHED_SPLASH
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_DRAW_FEED
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_REWARD_VIDEO
        //  CsjProvider.Native.nativeAdType = AdSlot.TYPE_SPLASH
        //--------------------------------------------------------------------------------------
        CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED

        //设置 穿山甲 图片可接受的尺寸 ( 建议设置，默认是 1080，607.5 )
        //CsjProvider.Native.setImageAcceptedSize(ScreenUtil.getDisplayMetricsWidth(this), ScreenUtil.getDisplayMetricsWidth(this) * 9 / 16)

        //指定普链广告点击后用于展示落地页的浏览器类型，可选项包括：InnerBrowser（APP 内置浏览器），Sys（系统浏览器），Default（默认，SDK 按照默认逻辑选择)
        // ( 非必须设置，默认是：BrowserType.Default )
        //GdtProvider.Native.browserType = BrowserType.Default

        //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非 wifi 展示），NoConfirm（所有情况不展示）
        // ( 非必须设置，默认是：DownAPPConfirmPolicy.Default )
        //GdtProvider.Native.downAPPConfirmPolicy = DownAPPConfirmPolicy.Default

        //设置返回视频广告的最大视频时长（闭区间，可单独设置），单位:秒，合法输入为：5<=maxVideoDuration<=60. 此设置会影响广告填充，请谨慎设置
        // ( 非必须设置，默认是：60 )
        //GdtProvider.Native.maxVideoDuration = 60

        //设置返回视频广告的最小视频时长（闭区间，可单独设置），单位:秒 此设置会影响广告填充，请谨慎设置
        // ( 非必须设置，默认是：5 )
        //GdtProvider.Native.minVideoDuration = 5

        //设置本次拉取的视频广告，从用户角度看到的视频播放策略；
        // 可选项包括自VideoOption.VideoPlayPolicy.AUTO(在用户看来，视频广告是自动播放的)和VideoOption.VideoPlayPolicy.MANUAL(在用户看来，视频广告是手动播放的)；
        // 如果广告位支持视频，强烈建议调用此接口设置视频广告的播放策略，有助于提高eCPM值；如果广告位不支持视频，忽略本接口
        // ( 默认是：VideoOption.VideoPlayPolicy.AUTO )
        //GdtProvider.Native.videoPlayPolicy = VideoOption.VideoPlayPolicy.AUTO

        adHelperNativeRv.getList(listener = object : NativeListener {
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

    override fun onResume() {
        super.onResume()
        adHelperNativeRv.resumeAllAd()
    }

    override fun onPause() {
        super.onPause()
        adHelperNativeRv.pauseAllAd()
    }

    override fun onDestroy() {
        super.onDestroy()
        adHelperNativeRv.destroyAllAd()
    }
}