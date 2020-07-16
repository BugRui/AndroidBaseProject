package com.bugrui.jetpackproject.widget.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.bugrui.jetpackproject.ext.dp

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/18 11:34
 * @Description:       java类作用描述
 */
/**
 * @Author: BugRui
 * @CreateDate: 2019/7/18 16:33
 * @Description: LinearLayoutManager(RecyclerView.VERTICAL)适配器分割线
 */
class ListItemDecoration : ItemDecoration {
    private var mSpace: Int
    private var isTopSpace: Boolean

    constructor(context: Context?) {
        mSpace = 8.dp
        isTopSpace = true
    }

    constructor(context: Context?, space: Int, isTopSpace: Boolean) {
        mSpace = space.dp
        this.isTopSpace = isTopSpace
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        if (position == 0) {
            outRect[0, if (isTopSpace) mSpace else 0, 0] = 0
        } else {
            outRect[0, mSpace, 0] = 0
        }
    }
}
