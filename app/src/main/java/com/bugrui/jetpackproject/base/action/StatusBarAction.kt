package com.bugrui.jetpackproject.base.action

import android.graphics.Color
import android.view.View
import androidx.annotation.ColorInt


/**
 * 状态栏
 */
interface StatusBarAction {

    /**
     * 获取状态栏
     */
    fun getStatusBarView():View

    /**
     * 显示状态栏
     */
    fun showStatusBar()

    /**
     * 隐藏状态栏
     */
    fun hideStatusBar()

    /**
     * 状态栏是否显示(默认显示)
     */
    fun onStatusBarVisibility(): Int = View.VISIBLE

    /**
     * 状态栏颜色
     */
    @ColorInt
    fun onStatusBarColor(): Int = Color.parseColor("#F9F9F9")

}