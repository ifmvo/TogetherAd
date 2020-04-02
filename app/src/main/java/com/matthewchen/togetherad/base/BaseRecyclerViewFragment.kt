package com.matthewchen.togetherad.base

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.matthewchen.togetherad.R
import com.matthewchen.togetherad.utils.CommonItemDecoration
import com.matthewchen.togetherad.utils.Kits

/*
 * (●ﾟωﾟ●)
 *
 * Created by Matthew_Chen on 2018/11/23.
 */
abstract class BaseRecyclerViewFragment<T, P : BaseViewHolder> : LazyFragment() {

    var mCurrentPage = 1
    lateinit var flTopView: FrameLayout
    lateinit var recyclerView: RecyclerView
    lateinit var rlParent: RelativeLayout
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var flBottomView: FrameLayout
    lateinit var llLoading: LinearLayout

    lateinit var mAdapter: BaseQuickAdapter<T, P>

    override fun onCreateViewLazy(savedInstanceState: Bundle?) {
        super.onCreateViewLazy(savedInstanceState)

        setContentView(R.layout.base_fragment_recylerview)

        flTopView = findViewById(R.id.flTopView) as FrameLayout
        flBottomView = findViewById(R.id.flBottomView) as FrameLayout
        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        llLoading = findViewById(R.id.llLoading) as LinearLayout
        rlParent = findViewById(R.id.rlParent) as RelativeLayout
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout

        initBeforeGetData()

        mAdapter = getRecyclerViewAdapter()

        recyclerView.layoutManager = getRecyclerViewLayoutManager()
        recyclerView.addItemDecoration(getRecyclerViewItemDecoration())

        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext,
            R.color.c_theme
        ), Color.BLACK)

        if (canRefresh()) {
            swipeRefreshLayout.setOnRefreshListener {
                mCurrentPage = 1
                getData(mCurrentPage, false)
            }
        }
        swipeRefreshLayout.isEnabled = canRefresh()

        if (canLoadMore()) {
            mAdapter.setOnLoadMoreListener({
                mCurrentPage++
                getData(mCurrentPage, false)
            }, recyclerView)
        }

        if (!canAutoLoadMore()) {
            mAdapter.disableLoadMoreIfNotFullPage(recyclerView)
        }

        recyclerView.adapter = mAdapter

        /*
         * 初始化完成，首次 显示 加载 loading ， 并请求数据
         */
        getData(mCurrentPage, false)
    }

    /**
     * 再网络请求的 error 回调使用
     */
    protected fun handleError(errorMsg: String) {
        swipeRefreshLayout.isRefreshing = false
        llLoading.visibility = View.GONE
        mAdapter.loadMoreFail()
        setEmpty(errorMsg)
    }

    /**
     * 在处理List 中 第一页就没有数据，会自动调用
     */
    protected fun setEmpty(msg: String) {
        if (showEmpty() && !TextUtils.isEmpty(msg)) {
            //没有数据
            val view = View.inflate(mContext, R.layout.view_empty, null)
            val tvText = view.findViewById<TextView>(R.id.tv_empty)
            tvText.text = msg
            mAdapter.emptyView = view
        }
    }

    /**
     * 统一处理 List 数据
     * @param listData
     * @param page
     */
    fun handleListData(listData: List<T>?, page: Int) {
        if (page == 1) {
            if (listData?.isNotEmpty() == true) {
                mAdapter.setNewData(listData)
            } else {
                mAdapter.setNewData(mutableListOf())
                setEmpty(getEmptyTxt())
            }
        } else {
            mAdapter.loadMoreComplete()

            if (listData?.isNotEmpty() == true) {
                mAdapter.addData(listData)
            } else {
                mAdapter.loadMoreEnd(false)
            }
        }
        swipeRefreshLayout.isRefreshing = false
        llLoading.visibility = View.GONE
    }

    /**
     * 必须重写
     */
    protected abstract fun initBeforeGetData()

    /**
     * 必须重写
     */
    protected abstract fun getRecyclerViewAdapter(): BaseQuickAdapter<T, P>

    /**
     * 必须重写
     */
    protected abstract fun getData(currentPage: Int, showLoading: Boolean)

    /**
     * 提供重写
     */
    open fun getRecyclerViewLayoutManager() =
        LinearLayoutManager(mContext)

    /**
     * 提供重写
     */
    open fun getRecyclerViewItemDecoration() =
        CommonItemDecoration(Kits.Dimens.dpToPx(mContext, 0.5f))

    /**
     * 提供重写 设置是否 下拉刷新
     */
    open fun canRefresh(): Boolean = true

    /**
     * 提供重写 设置是否 上拉加载
     */
    open fun canLoadMore(): Boolean = true

    /**
     * 提供重写 设置是否 在数据为空的时候显示空的界面
     */
    open fun showEmpty(): Boolean = true

    /**
     * 提供重写 是否到底，自动加载
     */
    open fun canAutoLoadMore(): Boolean = true

    /**
     * 提供重写
     */
    open fun getEmptyTxt(): String = "这里什么都没有"
}
