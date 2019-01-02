package com.matthewchen.togetherad.ui

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.baidu.mobad.feeds.NativeResponse
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.iflytek.voiceads.NativeADDataRef
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

    private var itemIvH: Int = 0

    override fun initBeforeGetData() {

        itemIvH = Kits.Dimens.getDisplayWidth(mContext) * 9 / 16
        //广告的曝光处理
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager
                if (layoutManager is LinearLayoutManager) {
                    val firstPosition = layoutManager.findFirstCompletelyVisibleItemPosition()
                    val lastPosition = layoutManager.findLastCompletelyVisibleItemPosition()

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
            val ad = item.adObject
            when (ad) {
                is NativeMediaADData -> {
                    ad.onExposured(recyclerView)
                }
                is NativeResponse -> {
                    ad.recordImpression(recyclerView)
                }
                is NativeADDataRef -> {
                    ad.onExposured(recyclerView)
                }
            }
        }
    }

    /**
     * 处理热播数据
     */
    private fun convertContent(helper: BaseViewHolder?, item: IndexMultiItemBean?) {
        val layoutParams = helper?.getView<ImageView>(R.id.iv_image)?.layoutParams
        layoutParams?.height = itemIvH

        ILFactory.getLoader()
            .load(mContext, helper?.getView(R.id.iv_image), item?.indexBean?.image, LoaderOptions())

        helper?.setText(R.id.tv_desc, "${item?.indexBean?.detail}")?.setText(R.id.tv_title, item?.indexBean?.title)
    }

    /**
     * 处理广点通广告的数据
     */
    private fun convertGDTAd(helper: BaseViewHolder?, item: IndexMultiItemBean?) {
        val mLlParent = helper?.getView<LinearLayout>(R.id.ll_parent)
        val mImgPoster = helper?.getView<ImageView>(R.id.img_poster)
        val mTvTitle = helper?.getView<TextView>(R.id.tv_title)
        val mAdGdtMediaPlayer = helper?.getView<MediaView>(R.id.gdt_media_view)

        val layoutParams = mImgPoster?.layoutParams
        layoutParams?.height = itemIvH

        val adObject = item?.adObject

        if (adObject is NativeMediaADData) {
            mTvTitle?.text = adObject.title
            ILFactory.getLoader().load(
                mContext, mImgPoster, adObject.imgUrl,
                LoaderOptions().skipCache()
            )

            mLlParent?.setOnClickListener {
                adObject.onClicked(it)
            }
            if (adObject.adPatternType == AdPatternType.NATIVE_VIDEO && adObject.isVideoLoaded) {
                if (adObject.isPlaying) {
                    mAdGdtMediaPlayer?.visibility = View.VISIBLE
                    mImgPoster?.visibility = View.GONE
                } else {
                    mAdGdtMediaPlayer?.visibility = View.VISIBLE
                    mImgPoster?.visibility = View.GONE
                    adObject.bindView(mAdGdtMediaPlayer, true) // 只有将MediaView和广告实例绑定之后，才能播放视频
                    adObject.play()
                    adObject.setMediaListener(object : MediaListener {
                        override fun onVideoReady(videoDuration: Long) {}
                        override fun onVideoStart() {}
                        override fun onVideoPause() {}
                        override fun onVideoComplete() {}
                        override fun onVideoError(adError: AdError) {}
                        override fun onReplayButtonClicked() {}
                        override fun onADButtonClicked() {
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
    }

    /**
     * 处理百度 mob 广告的数据
     */
    private fun convertBaiduAd(helper: BaseViewHolder?, item: IndexMultiItemBean?) {
        val mImgPoster = helper?.getView<ImageView>(R.id.img_poster)
        val mLlParent = helper?.getView<LinearLayout>(R.id.ll_parent)
        val mTvTitle = helper?.getView<TextView>(R.id.tv_title)

        val layoutParams = mImgPoster?.layoutParams
        layoutParams?.height = itemIvH

        val adObject = item?.adObject

        if (adObject is NativeResponse) {
            mTvTitle?.text = adObject.title
            ILFactory.getLoader().load(mContext, mImgPoster, adObject.imageUrl, LoaderOptions())
            mLlParent?.setOnClickListener {
                adObject.handleClick(it)
            }
        }
    }


    override fun getRecyclerViewAdapter(): BaseQuickAdapter<IndexMultiItemBean, BaseViewHolder> {
        mAdapter = object : BaseMultiItemQuickAdapter<IndexMultiItemBean, BaseViewHolder>(null) {

            init {
                addItemType(IndexMultiItemBean.TYPE_CONTENT, R.layout.list_item_index_hot)
                addItemType(IndexMultiItemBean.TYPE_AD_GDT, R.layout.list_item_index_hot_ad_gdt)
                addItemType(IndexMultiItemBean.TYPE_AD_BAIDU, R.layout.list_item_index_hot_ad_baidu)
            }

            override fun convert(helper: BaseViewHolder?, item: IndexMultiItemBean?) {
                val itemViewType = helper?.itemViewType
                when (itemViewType) {
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
                }
            }
        }

        mAdapter.setOnItemClickListener { _, _, _ ->
            DetailActivity.DetailAct.action(mContext)
        }
        return mAdapter
    }

    override fun getData(currentPage: Int, showLoading: Boolean) {

        val listData = mutableListOf<IndexBean>()
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 1}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 2}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 3}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 4}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 5}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 6}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 7}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 8}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 9}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 10}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 11}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 12}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 13}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 14}",
                "xxxxx"
            )
        )
        listData.add(
            IndexBean(
                "https://github.com/ifmvo/SomeImage/blob/master/banner4.png?raw=true",
                "${(currentPage - 1) * 15 + 15}",
                "xxxxx"
            )
        )
        loadAd(listData, currentPage)
    }

    private fun loadAd(t: List<IndexBean>, currentPage: Int) {

        TogetherAdFlow.getAdList(
            mContext,
            Config.listAdConfig(),
            TogetherAdConst.AD_FLOW_INDEX,
            object : TogetherAdFlow.AdListenerList {
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
                val any = adList[lastUseAdPosition]
                if (any is NativeMediaADData) {
                    multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_AD_GDT, any))
                } else if (any is NativeResponse) {
                    multiItemList.add(IndexMultiItemBean(IndexMultiItemBean.TYPE_AD_BAIDU, any))
                }
                lastUseAdPosition += 1
                nextAdPosition += 5
            }
        }
        return multiItemList
    }
}