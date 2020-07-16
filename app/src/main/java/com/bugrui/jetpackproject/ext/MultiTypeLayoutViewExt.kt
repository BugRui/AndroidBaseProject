package com.bugrui.jetpackproject.ext

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.bugrui.layout.*

/**
 * 创建一个空视图布局
 */
fun Context.createEmptyLayoutView(
    @DrawableRes resId: Int,
    ratio: String = "377:334",
    bias: Float = 0.4F,
    textStr: String?,
    @ColorInt textColor: Int = Color.parseColor("#A9B7B7"),
    textStrSize: Float = 14F
): _ConstraintLayout {
    return _ConstraintLayout(this).apply {
        lparams(width = layoutMatchParent, height = layoutMatchParent)

        //图标
        val iv = ImageView(context).apply {
            id = View.generateViewId()
            setImageResource(resId)
        }.lparams(width = 120.dp, height = 0) {
            startToStart = PARENT_ID
            topToTop = PARENT_ID
            endToEnd = PARENT_ID
            bottomToBottom = PARENT_ID
            dimensionRatio = ratio
            verticalBias = bias
        }.also { addView(it) }

        //文字
        TextView(context).apply {
            setTextColor(textColor)
            text = textStr
            textSize = textStrSize
        }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
            topMargin = 8.dp
            startToStart = PARENT_ID
            endToEnd = PARENT_ID
            topToBottom = iv.id
        }.also { addView(it) }

    }
}


/**
 * 无数据布局视图
 */
val Context.emptyBaseLayoutView: TextView
    get() {
        return TextView(this).apply {
            setParams(width = layoutMatchParent, height = layoutMatchParent)
            text = "暂无数据"
            setTextColor(Color.parseColor("#A9B7B7"))
            textSize = 16F
            gravity = Gravity.CENTER
        }
    }

/**
 * 加载布局视图
 */
val Context.loadingBaseLayoutView: _LinearLayout
    get() {
        return _LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            lparams(width = layoutMatchParent, height = layoutMatchParent) {
                gravity = Gravity.CENTER
            }


            //文字
            TextView(context).apply {
                setTextColor(Color.parseColor("#A9B7B7"))
                text = "正在加载…"
                textSize = 12F
            }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
                topMargin = 6.dp
                gravity = Gravity.CENTER
            }.also { addView(it) }

        }
    }

/**
 * 异常布局视图
 */
val Context.errorBaseLayoutView: _LinearLayout
    get() {
        return _LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            lparams(width = layoutMatchParent, height = layoutMatchParent) {
                gravity = Gravity.CENTER
            }


            TextView(context).apply {
                setTextColor(Color.parseColor("#A9B7B7"))
                text = "加载失败"
                textSize = 16F
            }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
                topMargin = 6.dp
                gravity = Gravity.CENTER
            }.also { addView(it) }

        }
    }

/**
 * 无网络布局视图
 */
val Context.noNetworkBaseLayoutView: _LinearLayout
    get() {
        return _LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER

            lparams(width = layoutMatchParent, height = layoutMatchParent) {
                gravity = Gravity.CENTER
            }


            TextView(context).apply {
                setTextColor(Color.parseColor("#A9B7B7"))
                text = "网络异常"
                textSize = 16F
            }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
                topMargin = 6.dp
                gravity = Gravity.CENTER
            }.also { addView(it) }

        }
    }