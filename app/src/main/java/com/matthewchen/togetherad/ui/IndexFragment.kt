package com.matthewchen.togetherad.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.*
import com.baidu.mobad.feeds.NativeResponse
import com.bytedance.sdk.openadsdk.TTAdConstant
import com.bytedance.sdk.openadsdk.TTFeedAd
import com.bytedance.sdk.openadsdk.TTNativeAd
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.ifmvo.imageloader.ILFactory
import com.ifmvo.imageloader.progress.LoaderOptions
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.base.BaseRecyclerViewFragment
import com.matthewchen.togetherad.bean.IndexBean
import com.matthewchen.togetherad.bean.IndexMultiItemBean
import com.matthewchen.togetherad.config.Config
import com.matthewchen.togetherad.config.TogetherAdConst
import com.matthewchen.togetherad.utils.Kits
import com.qq.e.ads.nativ.MediaListener
import com.qq.e.ads.nativ.MediaView
import com.qq.e.ads.nativ.NativeMediaADData
import com.qq.e.comm.constants.AdPatternType
import com.qq.e.comm.util.AdError
import com.rumtel.ad.helper.flow.TogetherAdFlow


/*
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2019/1/2.
 */
class IndexFragment : BaseRecyclerViewFragment<IndexMultiItemBean, BaseViewHolder>() {

    /**
     * Item 的高度为屏幕宽度的 9/16，因为一般视频的宽高比是16：9
     */
    private val itemIvH by lazy { Kits.Dimens.getDisplayWidth(mContext) * 9 / 16 }

    /**
     * 广点通的视频广告继续播放
     */
    override fun onResume() {
        super.onResume()
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val firstPosition = layoutManager.findFirstVisibleItemPosition()
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            for (index in firstPosition..lastPosition) {
                val item = mAdapter.getItem(index)
                if (item is IndexMultiItemBean && item.itemType != IndexMultiItemBean.TYPE_CONTENT) {
                    when (val ad = item.adObject) {
                        is NativeMediaADData -> { //广点通
                            ad.resume()
                        }
                    }
                }
            }
        }
    }

    /**
     * 广点通的视频广告需要暂停
     */
    override fun onPause() {
        super.onPause()
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val firstPosition = layoutManager.findFirstVisibleItemPosition()
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            for (index in firstPosition..lastPosition) {
                val item = mAdapter.getItem(index)
                if (item is IndexMultiItemBean && item.itemType != IndexMultiItemBean.TYPE_CONTENT) {
                    when (val ad = item.adObject) {
                        is NativeMediaADData -> { //广点通
                            ad.stop()
                        }
                    }
                }
            }
        }
    }

    /**
     * 广点通需要销毁
     */
    override fun onDestroy() {
        super.onDestroy()
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is LinearLayoutManager) {
            val firstPosition = layoutManager.findFirstVisibleItemPosition()
            val lastPosition = layoutManager.findLastVisibleItemPosition()
            for (index in firstPosition..lastPosition) {
                val item = mAdapter.getItem(index)
                if (item is IndexMultiItemBean && item.itemType != IndexMultiItemBean.TYPE_CONTENT) {
                    when (val ad = item.adObject) {
                        is NativeMediaADData -> { //广点通
                            ad.destroy()
                        }
                    }
                }
            }
        }
    }

    /**
     * RecyclerView 的滑动监听
     */
    override fun initBeforeGetData() {

        //广告的曝光处理
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstPosition = layoutManager.findFirstVisibleItemPosition()
                    val lastPosition = layoutManager.findLastVisibleItemPosition()
                    exposure(firstPosition)
                    if (firstPosition != lastPosition) {
                        exposure(lastPosition)
                    }
                }
            }
        })
    }

    /**
     * 广告的曝光处理
     */
    fun exposure(position: Int) {
        if (position < 0 || position > mAdapter.itemCount - 1) {
            return
        }
        val item = mAdapter.getItem(position)
        if (item is IndexMultiItemBean && item.itemType != IndexMultiItemBean.TYPE_CONTENT) {
            when (val ad = item.adObject) {
                is NativeMediaADData -> { //广点通
                    ad.onExposured(recyclerView)
                    if (ad.adPatternType == AdPatternType.NATIVE_VIDEO && ad.isVideoLoaded) {
                        //控制视频广告滑出屏幕滑进屏幕的暂停与播放
                        ad.onScroll(position, recyclerView)
                    }
                }
                is NativeResponse -> { //百度
                    ad.recordImpression(recyclerView)
                }
            }
        }
    }

    /**
     * 处理热播数据
     */
    private fun convertContent(helper: BaseViewHolder, item: IndexMultiItemBean) {
        val layoutParams = helper.getView<ImageView>(R.id.iv_image)?.layoutParams
        layoutParams?.height = itemIvH

        ILFactory.getLoader()
                .load(mContext, helper.getView(R.id.iv_image), item.indexBean?.image, LoaderOptions())

        helper.setText(R.id.tv_desc, "${item.indexBean?.detail}")
                ?.setText(R.id.tv_title, item.indexBean?.title)
    }

    /**
     * 处理广点通广告的数据
     */
    private fun convertGDTAd(helper: BaseViewHolder, item: IndexMultiItemBean) {
        val mLlSuper = helper.getView<LinearLayout>(R.id.ll_super)
        val mImgPoster = helper.getView<ImageView>(R.id.img_poster)
        val mTvTitle = helper.getView<TextView>(R.id.tv_title)
        val mTvDesc = helper.getView<TextView>(R.id.tv_desc)
        val mAdGdtMediaPlayer = helper.getView<MediaView>(R.id.gdt_media_view)

        val layoutParams = mImgPoster?.layoutParams
        layoutParams?.height = itemIvH

        val adObject = item.adObject

        if (adObject is NativeMediaADData) {
            mTvTitle?.text = adObject.title
            mTvDesc?.text = adObject.desc
            mImgPoster?.setImageResource(R.mipmap.ic_launcher)
            ILFactory.getLoader().load(mContext, mImgPoster, adObject.imgUrl, LoaderOptions().skipCache())

            mLlSuper?.setOnClickListener {
                adObject.onClicked(it)
            }
            if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO) {
                adObject.preLoadVideo()
            }

            if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO && adObject.isVideoLoaded) {
                mAdGdtMediaPlayer?.visibility = View.VISIBLE
                mImgPoster?.visibility = View.GONE
                adObject.bindView(mAdGdtMediaPlayer, true) // 只有将MediaView和广告实例绑定之后，才能播放视频
                adObject.play()
                adObject.setMediaListener(object : MediaListener {
                    override fun onVideoReady(videoDuration: Long) {
                        Log.d("ifmvo", "onVideoReady: $videoDuration")
                    }

                    override fun onVideoStart() {
                        Log.d("ifmvo", "onVideoStart")

                    }

                    override fun onVideoPause() {
                        Log.d("ifmvo", "onVideoPause")
                    }

                    override fun onVideoComplete() {
                        Log.d("ifmvo", "onVideoComplete")
                    }

                    override fun onVideoError(adError: AdError) {
                        Log.d("ifmvo", "onVideoError：${adError.errorCode}: ${adError.errorMsg}")
                    }

                    override fun onReplayButtonClicked() {
                        Log.d("ifmvo", "onReplayButtonClicked")
                    }

                    override fun onADButtonClicked() {
                        Log.d("ifmvo", "onADButtonClicked")
                    }

                    override fun onFullScreenChanged(inFullScreen: Boolean) {
                        if (inFullScreen) {
                            adObject.setVolumeOn(true)
                        } else {
                            adObject.setVolumeOn(false)
                        }
                    }
                })
            }
        }
    }

    /**
     * 处理百度 mob 广告的数据
     */
    private fun convertBaiduAd(helper: BaseViewHolder, item: IndexMultiItemBean) {
        val mImgPoster = helper.getView<ImageView>(R.id.img_poster)
        val mLlSuper = helper.getView<LinearLayout>(R.id.ll_super)
        val mTvTitle = helper.getView<TextView>(R.id.tv_title)

        val layoutParams = mImgPoster?.layoutParams
        layoutParams?.height = itemIvH

        val adObject = item.adObject

        if (adObject is NativeResponse) {
            mTvTitle?.text = adObject.title
            ILFactory.getLoader().load(mContext, mImgPoster, adObject.imageUrl, LoaderOptions())
            mLlSuper?.setOnClickListener {
                adObject.handleClick(it)
            }
        }
    }

    /**
     * 处理穿山甲的广告数据
     */
    private fun convertCsjAd(helper: BaseViewHolder, item: IndexMultiItemBean) {
        val mLlSuper = helper.getView<LinearLayout>(R.id.ll_super)

        val mFlParent = helper.getView<FrameLayout>(R.id.fl_parent)

        //图片类型的
        val mLlAdContainer = helper.getView<LinearLayout>(R.id.ll_ad_container)
        val mImgPoster0 = helper.getView<ImageView>(R.id.img_poster1)
        val mImgPoster1 = helper.getView<ImageView>(R.id.img_poster2)
        val mImgPoster2 = helper.getView<ImageView>(R.id.img_poster3)
        //视频类型的
        val mFlVideoContainer = helper.getView<FrameLayout>(R.id.fl_ad_container)

        //标题和描述
        val mTvTitle = helper.getView<TextView>(R.id.tv_title)
        val mTvDesc = helper.getView<TextView>(R.id.tv_desc)
        val mIvAdLogo = helper.getView<ImageView>(R.id.iv_csj_logo)

        val layoutParams = mFlParent?.layoutParams
        layoutParams?.height = itemIvH

        val adObject = item.adObject

        if (adObject is TTFeedAd) {
            mIvAdLogo.setImageBitmap(adObject.adLogo)
            // 可以被点击的view, 也可以把convertView放进来意味整个item可被点击，点击会跳转到落地页
            val clickViewList = mutableListOf<View>()
            clickViewList.add(mLlSuper)
            // 创意点击区域的view 点击根据不同的创意进行下载或拨打电话动作
            //如果需要点击图文区域也能进行下载或者拨打电话动作，请将图文区域的view传入creativeViewList
            val creativeViewList = mutableListOf<View>()
            creativeViewList.add(mLlSuper)
            // 注册普通点击区域，创意点击区域。重要! 这个涉及到广告计费及交互，必须正确调用。convertView必须使用ViewGroup。
            adObject.registerViewForInteraction(mLlSuper, clickViewList, creativeViewList,
                    object : TTNativeAd.AdInteractionListener {
                        override fun onAdClicked(view: View, ad: TTNativeAd) {
                            // 点击普通区域的回调
                            Toast.makeText(mContext, "广告被点击", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdCreativeClick(view: View, ad: TTNativeAd) {
                            // 点击创意区域的回调
                            Toast.makeText(mContext, "广告创意按钮被点击", Toast.LENGTH_SHORT).show()
                        }

                        override fun onAdShow(ad: TTNativeAd) {
                            // 广告曝光展示的回调
                            Toast.makeText(mContext, "广告" + ad.title + "展示", Toast.LENGTH_SHORT).show()
                        }
                    })

            mTvTitle.text = adObject.title
            mTvDesc.text = adObject.description
            Log.e("ifmvo", "adObject.imageMode: ${adObject.imageMode}")
            when (adObject.imageMode) {
                //视频类型
                TTAdConstant.IMAGE_MODE_VIDEO, TTAdConstant.IMAGE_MODE_VIDEO_VERTICAL -> {

                    mLlAdContainer.visibility = View.VISIBLE
                    mFlVideoContainer.visibility = View.GONE
                    mImgPoster1.visibility = View.GONE
                    mImgPoster2.visibility = View.GONE
                    val videoCoverImage = adObject.videoCoverImage

                    if (videoCoverImage != null && videoCoverImage.imageUrl != null) {
                        ILFactory.getLoader().load(mContext, mImgPoster0, videoCoverImage.imageUrl)
                    }

//                    mLlAdContainer.visibility = View.GONE
//                    mFlVideoContainer.visibility = View.VISIBLE
//                    val video = adObject.adView
//                    if (video != null && video.parent == null) {
//                        mFlVideoContainer.removeAllViews()
//                        mFlVideoContainer.addView(video)
//                        adObject.setVideoAdListener(object : TTFeedAd.VideoAdListener {
//                            override fun onVideoLoad(ad: TTFeedAd) {
//                                Log.e("ifmvo", "onVideoLoad")
//                            }
//
//                            override fun onVideoError(errorCode: Int, extraCode: Int) {
//                                Log.e("ifmvo", "onVideoError")
//                            }
//
//                            override fun onVideoAdStartPlay(ad: TTFeedAd) {
//                                Log.e("ifmvo", "onVideoAdStartPlay")
//                            }
//
//                            override fun onVideoAdPaused(ad: TTFeedAd) {
//                                Log.e("ifmvo", "onVideoAdPaused")
//                            }
//
//                            override fun onVideoAdContinuePlay(ad: TTFeedAd) {
//                                //因为点击广告后返回来，出现了黑屏的情况，所以这里当继续播放的时候就刷新一下这个广告，初始化这个广告的状态
////                                mAdapter.notifyItemChanged(helper.adapterPosition)
//                            }
//                        })
//                    }
                }
                //单个图片的类型
                TTAdConstant.IMAGE_MODE_LARGE_IMG, TTAdConstant.IMAGE_MODE_SMALL_IMG, TTAdConstant.IMAGE_MODE_VERTICAL_IMG -> {
                    mLlAdContainer.visibility = View.VISIBLE
                    mFlVideoContainer.visibility = View.GONE
                    mImgPoster1.visibility = View.GONE
                    mImgPoster2.visibility = View.GONE
                    val imageList = adObject.imageList

                    if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                        ILFactory.getLoader().load(mContext, mImgPoster0, imageList[0].imageUrl)
                    }
                }
                //多个图片的类型
                TTAdConstant.IMAGE_MODE_GROUP_IMG -> {
                    mLlAdContainer.visibility = View.VISIBLE
                    mFlVideoContainer.visibility = View.GONE
                    mImgPoster1.visibility = View.VISIBLE
                    mImgPoster2.visibility = View.VISIBLE
                    val imageList = adObject.imageList

                    if (imageList.isNotEmpty() && imageList[0] != null && imageList[0].isValid) {
                        ILFactory.getLoader().load(mContext, mImgPoster0, imageList[0].imageUrl)
                    }
                    if (imageList.isNotEmpty() && imageList.size > 1 && imageList[1] != null && imageList[1].isValid) {
                        ILFactory.getLoader().load(mContext, mImgPoster1, imageList[1].imageUrl)
                    }
                    if (imageList.isNotEmpty() && imageList.size > 2 && imageList[2].isValid) {
                        ILFactory.getLoader().load(mContext, mImgPoster2, imageList[2].imageUrl)
                    }
                }
            }
        }
    }

    /**
     * 初始化Adapter
     */
    override fun getRecyclerViewAdapter(): BaseQuickAdapter<IndexMultiItemBean, BaseViewHolder> {
        mAdapter = object : BaseMultiItemQuickAdapter<IndexMultiItemBean, BaseViewHolder>(null) {

            init {
                addItemType(IndexMultiItemBean.TYPE_CONTENT, R.layout.list_item_index_hot)
                addItemType(IndexMultiItemBean.TYPE_AD_GDT, R.layout.list_item_index_hot_ad_gdt)
                addItemType(IndexMultiItemBean.TYPE_AD_BAIDU, R.layout.list_item_index_hot_ad_baidu)
                addItemType(IndexMultiItemBean.TYPE_AD_CSJ, R.layout.list_item_index_hot_ad_csj)
            }

            override fun convert(helper: BaseViewHolder, item: IndexMultiItemBean) {
                when (helper.itemViewType) {
                    //热播数据
                    IndexMultiItemBean.TYPE_CONTENT -> {
                        convertContent(helper, item)
                    }
                    //广告 GDT
                    IndexMultiItemBean.TYPE_AD_GDT -> {
                        convertGDTAd(helper, item)
                    }
                    //广告 Baidu Mob
                    IndexMultiItemBean.TYPE_AD_BAIDU -> {
                        convertBaiduAd(helper, item)
                    }
                    //广告穿山甲
                    IndexMultiItemBean.TYPE_AD_CSJ -> {
                        convertCsjAd(helper, item)
                    }
                }
            }
        }

        mAdapter.setOnItemClickListener { _, _, _ ->
            DetailActivity.DetailAct.action(mContext)
        }
        return mAdapter
    }

    /**
     * 开始获取第n页的数据
     */
    override fun getData(currentPage: Int, showLoading: Boolean) {
        val indexBeanList = DataFactory.getIndexDatas(currentPage)
        loadAd(indexBeanList, currentPage)
    }

    /**
     * 请求广告
     */
    private fun loadAd(t: List<IndexBean>, currentPage: Int) {
        TogetherAdFlow.getAdList(mContext, Config.listAdConfig(), TogetherAdConst.AD_FLOW_INDEX, object : TogetherAdFlow.AdListenerList {
            override fun onAdFailed(failedMsg: String?) {
                handleListData(insertAdAction(t, null), currentPage)
            }

            override fun onAdLoaded(channel: String, adList: List<*>) {
                handleListData(insertAdAction(t, adList), currentPage)
            }

            override fun onStartRequest(channel: String) {
            }
        })
    }

    /**
     * 插入广告的处理
     */
    private fun insertAdAction(t: List<IndexBean>, adList: List<*>?): List<IndexMultiItemBean> {

        var nextAdPosition = 0
        var lastUseAdPosition = 0

        val multiItemList = ArrayList<IndexMultiItemBean>()
        repeat(t.size) {
            multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_CONTENT, t[it]))
            if (adList != null && nextAdPosition == it) {
                if (lastUseAdPosition > adList.size - 1) {
                    lastUseAdPosition = 0
                }
                when (val any = adList[lastUseAdPosition]) {
                    is NativeMediaADData -> multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_AD_GDT, any))
                    is NativeResponse -> multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_AD_BAIDU, any))
                    is TTFeedAd -> multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_AD_CSJ, any))
                }
                lastUseAdPosition += 1
                nextAdPosition += 5
            }
        }
        return multiItemList
    }
}