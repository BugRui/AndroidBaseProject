package com.bugrui.jetpackproject.widget.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/24 11:32
 * @Description:       java类作用描述
 */
class GridLayoutItemDecoration : RecyclerView.ItemDecoration() {
    private var canvas: Canvas? = null
    /**
     * 复写onDraw方法，从而达到在每隔条目的被绘制之前
     * @param c
     * @param parent
     * @param state
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        //先初始化一个Paint来简单指定一下Canvas的颜色
        val paint = Paint()
        canvas = c
        paint.color = Color.parseColor("#d1d1d1")

        //获得RecyclerView中总条目数量
        val childCount = parent.childCount
        // viewGroup的bottomY值存储
        val viewGroup = ArrayList<BottomLineDot>()
        //遍历一下
        var width = 0
        var height = 0
        for (i in 0 until childCount) {
            //获得子View，也就是一个条目的View，准备给他画上边框
            val childView = parent.getChildAt(i)
            //先获得子View的长宽，以及在屏幕上的位置，方便我们得到边框的具体坐标
            val x = childView.x
            val y = childView.y
            width = childView.width
            height = childView.height
            //根据这些点画条目的四周的线
            // 上line
            //            c.drawLine(x, y, x + width, y, paint);
            // 左 line
            c.drawLine(x, y, x, y + height, paint)
            // 右 line
            c.drawLine(x + width, y, x + width, y + height, paint)
            // 画底部的边线
            val tempBottomY = y + height
            viewGroup.add(BottomLineDot(x, tempBottomY, x + width, y + height, paint))
            //            if (i == childCount - 1) {
            c.drawLine(x, tempBottomY - 1, x + width, y + height - 1, paint)
            //            } else {
            //                c.drawLine(x, tempBottomY, x + width, y + height, paint);
            //            }
            //要根据自己不同的设计稿进行画线...
        }
        drawTopAndBottomLine(viewGroup, width, height)
        super.onDraw(c, parent, state)
    }

    private fun drawTopAndBottomLine(viewGroup: ArrayList<BottomLineDot>, width: Int, height: Int) {
        if (viewGroup.size == 0) {
            return
        }
        val frist = viewGroup[0]
        val last = viewGroup[viewGroup.size - 1]
        // 从最后一个来比较和最前一个
        for (i in viewGroup.indices.reversed()) {
            val item = viewGroup[i]

            if (last.startY == item.startY) {
                canvas!!.drawLine(
                    item.startX,
                    item.startY - 1,
                    item.endX,
                    item.endY - 1,
                    item.paint!!
                )
            } else if (item.startY == frist.startY) {
                canvas!!.drawLine(
                    item.startX,
                    item.startY - height,
                    item.endX,
                    item.endY - height,
                    item.paint!!
                )
            }
        }
    }

    private class BottomLineDot(
        var startX: Float,
        var startY: Float,
        var endX: Float,
        var endY: Float,
        var paint: Paint?
    )
}