package com.bugrui.jetpackproject.common

import com.drakeet.multitype.MultiTypeAdapter

class ListMultiTypeAdapter : MultiTypeAdapter() {

    val itemList = ArrayList<Any>()

    /**
     * 刷新列表
     */
    fun notifyRefresh(list: List<Any>?) {
        itemList.clear()
        itemList.addAll(list ?: emptyList())
        this.notifyDataSetChanged()
    }

    /**
     * 列表数据加载
     */
    fun notifyLoadMore(list: List<Any>?) {
        if (list == null || list.isEmpty()) return
        val oldCount = itemList.size
        itemList.addAll(list)
        val newCount = itemList.size
        this.notifyItemRangeInserted(oldCount, newCount)

    }

    /**
     * 删除某item
     */
    fun notifyRemoveAt(position: Int) {
        if (this.items.size - 1 >= position) {
            itemList.removeAt(position)
            this.notifyItemRemoved(position)
        }
    }

}