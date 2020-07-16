package com.bugrui.jetpackproject.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.bugrui.jetpackproject.base.action.CommonListAction
import com.bugrui.jetpackproject.common.ListMultiTypeAdapter
import com.bugrui.jetpackproject.ext.bindAdapter
import com.bugrui.jetpackproject.ext.toast
import com.scwang.smart.refresh.layout.api.RefreshLayout
import com.scwang.smart.refresh.layout.listener.OnRefreshLoadMoreListener

abstract class BaseListFragment<T : Any> : BaseFragment(),
    CommonListAction<T>,
    OnRefreshLoadMoreListener {

    lateinit var multiTypeAdapter: ListMultiTypeAdapter
    private var pagingPage = 1

    override fun onCustomizeViewCreated(view: View, savedInstanceState: Bundle?) {
        multiTypeAdapter = ListMultiTypeAdapter()
        onAdapterRegister()
        onFindViewById()
        checkIfTheViewThatMustBeBoundIsEmpty()
        bindSmartRefreshLayout()?.setEnableLoadMore(isLoadNextPage())
        bindSmartRefreshLayout()?.setEnableLoadMoreWhenContentNotFull(false)
        bindSmartRefreshLayout()?.setOnRefreshLoadMoreListener(this)
        bindRecyclerView()?.bindAdapter(
            adapter = multiTypeAdapter,
            layoutManager = bindLayoutManager()!!,
            hasFixedSize = hasListFixedSize(),
            hasLayoutAnimation = hasLayoutAnimation()
        )
    }

    override fun checkIfTheViewThatMustBeBoundIsEmpty() {
        if (bindRecyclerView() == null) throw NullPointerException("bindRecyclerView can not be empty")
        if (bindLayoutManager() == null) throw NullPointerException("bindLayoutManager can not be empty")
    }

    override fun onRefresh(refreshLayout: RefreshLayout) = getListData(true)

    override fun onLoadMore(refreshLayout: RefreshLayout) = getListData(false)

    override fun refreshListData(list: List<T>?) {
        multiTypeAdapter.notifyRefresh(list)
    }

    override fun loadListData(list: List<T>?) {
        multiTypeAdapter.notifyLoadMore(list)
    }

    override fun getListData(isRefresh: Boolean) {
        bindDataList(if (isRefresh) 1 else pagingPage).observe(this, Observer {
            hideLoading()
            bindSmartRefreshLayout()?.finishRefresh(it.isSuccessful())
            bindSmartRefreshLayout()?.finishLoadMore(it.isSuccessful())
            bindMultiTypeLayout()?.showContent()
            if (it.isSuccessful()) {
                if (isRefresh) {
                    pagingPage = 2
                    refreshListData(it.data)
                    bindSmartRefreshLayout()?.setNoMoreData(false)
                    if (hasLayoutAnimation()) {
                        bindRecyclerView()?.scheduleLayoutAnimation()
                    }
                    if (multiTypeAdapter.items.isEmpty()) {
                        bindMultiTypeLayout()?.showEmpty()
                    }
                } else {
                    pagingPage += 1
                    loadListData(it.data)
                }
                if (it.data.isNullOrEmpty()) {
                    bindSmartRefreshLayout()?.finishLoadMoreWithNoMoreData()
                }
            } else {
                toast(it.msg)
                if (isRefresh) {
                    bindMultiTypeLayout()?.showError()
                }
            }
        })
    }
}