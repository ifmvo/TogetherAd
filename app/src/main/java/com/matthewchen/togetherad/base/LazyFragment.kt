package com.matthewchen.togetherad.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout

/* 
 * (●ﾟωﾟ●)
 * 
 * Created by Matthew_Chen on 2018/11/23.
 */
abstract class LazyFragment : Fragment() {

    private val stateVisible = 1 //用户可见
    private val stateNoSet = -1 //未设置值
    private val stateNoVisible = 0  //用户不可见

    lateinit var mContext: AppCompatActivity

    private var rootView: View? = null
    private var container: ViewGroup? = null

    private var isInitReady = false
    private var isVisibleToUserState = stateNoSet
    private var saveInstanceState: Bundle? = null
    private val isLazyEnable = true
    private var isStart = false
    private var layout: FrameLayout? = null

    private val tagRootFrameLayout = "tag_root_framelayout"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        this.container = container

        val isVisible: Boolean =
            if (isVisibleToUserState == stateNoSet) userVisibleHint else isVisibleToUserState == stateVisible

        if (isLazyEnable) {
            if (isVisible && !isInitReady) {
                onCreateViewLazy(savedInstanceState)
                isInitReady = true
            } else {
                var mInflater = layoutInflater
                if (mInflater == null) {
                    mInflater = LayoutInflater.from(mContext)
                }
                layout = FrameLayout(mContext)
                layout!!.tag = tagRootFrameLayout

                val view = getPreviewLayout(mInflater, layout)
                if (view != null) {
                    layout!!.addView(view)
                }
                layout!!.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setContentView(layout)
            }
        } else {
            onCreateViewLazy(savedInstanceState)
            isInitReady = true
        }
        return if (rootView == null) super.onCreateView(inflater, container, savedInstanceState) else rootView
    }

    protected fun findViewById(id: Int): View? {
        return rootView?.findViewById(id)
    }

    protected fun setContentView(layoutResID: Int) {
        if (isLazyEnable && rootView?.parent != null) {
            layout!!.removeAllViews()
            val view = layoutInflater!!.inflate(layoutResID, layout, false)
            layout!!.addView(view)
        } else {
            rootView = layoutInflater!!.inflate(layoutResID, container, false)
        }
    }

    protected fun setContentView(view: View?) {
        if (isLazyEnable && rootView?.parent != null) {
            layout?.removeAllViews()
            layout?.addView(view)
        } else {
            rootView = view
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isVisibleToUserState = if (isVisibleToUser) stateVisible else stateNoVisible
        if (isVisibleToUser && !isInitReady && rootView != null) {
            isInitReady = true
            onCreateViewLazy(saveInstanceState)
            onResumeLazy()
        }
        if (isInitReady && rootView != null) {
            if (isVisibleToUser) {
                isStart = true
                onStartLazy()
            } else {
                isStart = false
                onStopLazy()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isInitReady) {
            onResumeLazy()
        }
    }

    override fun onPause() {
        super.onPause()
        if (isInitReady) {
            onPauseLazy()
        }
    }

    override fun onStart() {
        super.onStart()
        if (isInitReady && !isStart && userVisibleHint) {
            isStart = true
            onStartLazy()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isInitReady && isStart && userVisibleHint) {
            isStart = false
            onStopLazy()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is AppCompatActivity) {
            this.mContext = context
        }
    }


    override fun onDetach() {
        super.onDetach()
        try {
            val childFragmentManager = Fragment::class.java.getDeclaredField("mChildFragmentManager")
            childFragmentManager.isAccessible = true
            childFragmentManager.set(this, null)
        } catch (e: NoSuchFieldException) {
            throw RuntimeException(e)
        } catch (e: IllegalAccessException) {
            throw RuntimeException(e)
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        rootView = null
        container = null
        if (isInitReady) onDestoryLazy()
        isInitReady = false
    }

    protected fun getPreviewLayout(mInflater: LayoutInflater, layout: FrameLayout?): View? {
        return null
    }

    open fun onCreateViewLazy(savedInstanceState: Bundle?) {

    }

    open fun onStartLazy() {

    }

    open fun onStopLazy() {

    }

    open fun onResumeLazy() {

    }

    open fun onPauseLazy() {

    }

    open fun onDestoryLazy() {

    }


}