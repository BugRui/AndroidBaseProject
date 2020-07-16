package com.bugrui.jetpackproject.ext

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.bugrui.jetpackproject.base.BasicApp

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/24 9:48
 * @Description:       资源扩展
 */
/**
 * 转Color
 */
val Int.toColorInt: Int
    @ColorInt
    get() = ContextCompat.getColor(BasicApp.sApp, this)

/**
 * 转Drawable
 */
val Int.toDrawable: Drawable?
    get() = ContextCompat.getDrawable(BasicApp.sApp, this)