package com.bugrui.jetpackproject.base.action

import android.graphics.Color
import android.graphics.Typeface
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import com.bugrui.jetpackproject.R

/**
 * 标题栏
 */
interface ToolbarAction {

    /**
     * 显示标题栏
     */
    fun showToolbar()

    /**
     * 隐藏标题栏
     */
    fun hideToolbar()

    /**
     * 设置标题栏背景色
     */
    @ColorInt
    fun onToolbarBackgroundColor(): Int = Color.parseColor("#F9F9F9")


    /**
     * 设置返回键监听器
     */
    fun setBackButtonListener(listener:OnActivityBackButtonListener)


    /**
     * 返回键是否可见(默认可见)
     */
    fun onBackButtonVisibility(): Int = View.VISIBLE


    /**
     * 显示返回键
     */
    fun showBackButton()

    /**
     * 隐藏返回键
     */
    fun hideBackButton()


    /**
     * 返回键文字
     */
    fun onBackButtonText(): String? = "返回"

    /**
     * 返回键文字大小
     */
    fun onBackButtonTextSize(): Float = 16F

    /**
     * 返回键文字颜色
     */
    @ColorInt
    fun onBackButtonTextColor(): Int = Color.parseColor("#333333")

    /**
     * 返回键左边图标
     */
    @DrawableRes
    fun onBackButtonLeftIcon(): Int = R.drawable.icon_back_black


    /**
     * 设置标题
     */
    fun setToolbarTitle(title: String?)

    /**
     * 标题字体大小
     */
    fun onTitleTextSize(): Float = 18F

    /**
     * 标题字体类型（默认加粗BOLD）
     */
    fun onTitleTypeface(): Typeface = Typeface.defaultFromStyle(Typeface.BOLD)

    /**
     * 标题文字颜色
     */
    @ColorInt
    fun onTitleTextColor(): Int = Color.parseColor("#333333")


    /**
     * 设置右边文字按钮
     */
    fun setRightTextButton(text: String?)

    /**
     * 显示右边文字按钮
     */
    fun showRightTextButton()

    /**
     * 隐藏右边文字按钮
     */
    fun hideRightTextButton()

    /**
     * 右边文字按钮文字大小
     */
    fun onRightButtonTextSize(): Float = 16F

    /**
     * 右边文字按钮字颜色
     */
    @ColorInt
    fun onRightButtonTextColor(): Int = Color.parseColor("#333333")


    /**
     * 设置右边图标按钮
     */
    fun setRightIconButton(@DrawableRes res: Int)

    /**
     * 显示右边图标按钮
     */
    fun showRightIconButton()

    /**
     * 隐藏右边图标按钮
     */
    fun hideRightIconButton()

    /**
     * 右边图标按钮图标
     */
    @DrawableRes
    fun onRightIconDrawableRes(): Int = R.drawable.icon_dian_dian

}

interface OnActivityBackButtonListener {
    fun onBackClick(view: View)
}