package com.bugrui.jetpackproject.ext

import android.app.Activity
import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/13 14:19
 * @Description:       键盘相关扩展方法
 * <在manifest.xml中activity中设置<
 *  <android:windowSoftInputMode="adjustPan"
 */

/**
 * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘
 */
fun isShouldHideKeyboard(v: View, event: MotionEvent): Boolean {
    if (v is EditText) {
        val l = intArrayOf(0, 0)
        v.getLocationInWindow(l)
        val left = l[0]
        val top = l[1]
        val bottom = top + v.getHeight()
        val right = left + v.getWidth()
        return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
    }
    return false
}

/**
 * Activity动态显示软键盘
 */
fun Activity.showSoftKeyboard() {
    var view = currentFocus
    if (view == null) view = View(this)
    val imm =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
}

/**
 * View动态显示软键盘
 */
fun View.showSoftKeyboard() {
//    isFocusable = true
//    isFocusableInTouchMode = true
    requestFocus()
    val imm =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
}

/**
 * Activity动态隐藏软键盘
 */
fun Activity.hideSoftKeyboard() {
    var view = currentFocus
    if (view == null) {
        view = View(this)

    }
    view.clearFocus()
    val imm =
        getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        view.windowToken,
        WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
    )
}

/**
 * View动态隐藏软键盘
 */
fun View.hideSoftKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    if (windowToken != null) {
        clearFocus()
        imm.hideSoftInputFromWindow(
            windowToken,
            WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
        )
    }
}

/**
 * 切换键盘显示与否状态
 */
fun Context.toggleSoftKeyboard() {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.toggleSoftInput(
        InputMethodManager.SHOW_FORCED,
        WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED
    )
}