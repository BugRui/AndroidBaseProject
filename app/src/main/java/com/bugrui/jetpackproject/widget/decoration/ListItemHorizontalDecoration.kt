package com.bugrui.jetpackproject.widget.decoration

import android.content.Context
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bugrui.jetpackproject.ext.px

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/27 10:52
 * @Description:       java类作用描述
 */
class ListItemHorizontalDecoration(space: Int) : RecyclerView.ItemDecoration() {

    private val mSpace: Int = space.px

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {

        val position = parent.getChildAdapterPosition(view)

        if (position == 0) {
            outRect.set(mSpace, mSpace, mSpace, mSpace)
        } else {
            outRect.set(0, mSpace, mSpace, mSpace)
        }

    }
}