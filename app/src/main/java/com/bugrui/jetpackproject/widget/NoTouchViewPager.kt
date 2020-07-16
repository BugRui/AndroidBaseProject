package com.bugrui.jetpackproject.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import androidx.viewpager.widget.ViewPager

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/6 16:36
 * @Description: 禁止左右滑动的ViewPager
 */
class NoTouchViewPager : ViewPager {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }

    /**
     * 禁止左右滑动动画
     *
     * @param item
     */
    override fun setCurrentItem(item: Int) {
        super.setCurrentItem(item, false)
    }
}
