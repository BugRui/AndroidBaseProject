package com.bugrui.jetpackproject.base.action

import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bugrui.jetpackproject.http.ApiResponse
import com.bugrui.library.MultiTypeLayout
import com.scwang.smart.refresh.layout.SmartRefreshLayout


interface CommonListAction<T> {

    /**
     * 绑定SmartRefreshLayout
     */
    fun bindSmartRefreshLayout(): SmartRefreshLayout?

    /**
     * 绑定RecyclerView
     */
    fun bindRecyclerView(): RecyclerView?

    /**
     * 绑定LayoutManager
     */
    fun bindLayoutManager(): RecyclerView.LayoutManager?

    /**
     * 绑定MultiTypeLayout
     */
    fun bindMultiTypeLayout(): MultiTypeLayout?

    /**
     * 绑定列表数据接口
     */
    fun bindDataList(pagingPage: Int): LiveData<ApiResponse<List<T>?>>

    /**
     * 是否开启分页加载
     */
    fun isLoadNextPage(): Boolean = true

    /**
     * item是固定大小？
     */
    fun hasListFixedSize(): Boolean = true

    /**
     * 是否开启刷新动画
     */
    fun hasLayoutAnimation(): Boolean = true

    /**
     * 多类型适配器注册
     */
    fun onAdapterRegister()

    /**
     * 做findViewById操作
     */
    fun onFindViewById()

    /**
     * 检查必须绑定的view是否为空
     */
    fun checkIfTheViewThatMustBeBoundIsEmpty()

    /**
     * 刷新列表数据
     */
    fun refreshListData(list: List<T>?)

    /**
     * 加载列表数据
     */
    fun loadListData(list: List<T>?)

    /**
     * 获取列表数据
     * @param isRefresh true 刷新列表，false 加载分页数据
     */
    fun getListData(isRefresh: Boolean)

}