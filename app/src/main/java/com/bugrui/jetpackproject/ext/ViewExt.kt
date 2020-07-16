package com.bugrui.jetpackproject.ext

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/13 13:59
 * @Description:       View相关扩展方法
 */


/**
 * 设置edtText是否可输入
 */
fun EditText.setEdit(input: Boolean) {
    isFocusable = input
    isEnabled = input
    isClickable = input
    isFocusableInTouchMode = input
}

/**
 * 设置TextView是否可点击
 */
fun TextView.setClick(isClick: Boolean) {
    isEnabled = isClick
    isClickable = isClick
}



/**
 * 设置view大小
 */
fun View.setSize(width: Int, height: Int) {
    if (layoutParams != null) {
        val params = layoutParams
        params.width = width
        params.height = height
        layoutParams = params
    } else {
        layoutParams = ViewGroup.LayoutParams(width, height)
    }
}

/***
 * 设置延迟时间的View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @return T
 */
fun <T : View> T.withTrigger(delay: Long = 600): T {
    triggerDelay = delay
    return this
}

/***
 * 点击事件的View扩展
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.click(block: (View) -> Unit) = setOnClickListener {

    if (clickEnable()) {
        block(it)
    }
}

/***
 * 带延迟过滤的点击事件View扩展
 * @param delay Long 延迟时间，默认600毫秒
 * @param block: (T) -> Unit 函数
 * @return Unit
 */
fun <T : View> T.clickWithTrigger(time: Long = 600, block: (View) -> Unit) {
    triggerDelay = time
    setOnClickListener {
        if (clickEnable()) {
            block(it)
        }
    }
}

private var <T : View> T.triggerLastTime: Long
    get() = if (getTag(1123460103) != null) getTag(1123460103) as Long else 0
    set(value) {
        setTag(1123460103, value)
    }

private var <T : View> T.triggerDelay: Long
    get() = if (getTag(1123461123) != null) getTag(1123461123) as Long else -1
    set(value) {
        setTag(1123461123, value)
    }

private fun <T : View> T.clickEnable(): Boolean {
    var flag = false
    val currentClickTime = System.currentTimeMillis()
    if (currentClickTime - triggerLastTime >= triggerDelay) {
        flag = true
    }
    triggerLastTime = currentClickTime
    return flag
}

