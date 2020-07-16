package com.bugrui.jetpackproject.dialog

import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.ext.dp
import com.bugrui.jetpackproject.ext.screenHeight
import com.bugrui.jetpackproject.ext.statusBarHeight
import com.bugrui.jetpackproject.ext.toColorInt
import com.bugrui.layout._LinearLayout
import com.bugrui.layout.layoutWrapContent

class LoadingDialog constructor(
    context: Context,
    attrs: Int = R.style.UniversalDialogStyle
) : Dialog(context, attrs) {

    private lateinit var tvMessage: TextView

    private val progressLayoutView by lazy {
        _LinearLayout(context).apply {
            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL
            lparams(width = layoutWrapContent, height = layoutWrapContent) {
                gravity = Gravity.CENTER
            }
            CardView(context).apply {
                setCardBackgroundColor(Color.parseColor("#B3363636"))
                radius = 18F
                cardElevation = 0F
                gravity = Gravity.CENTER
                _LinearLayout(context).apply {
                    gravity = Gravity.CENTER
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(8.dp, 8.dp, 8.dp, 8.dp)

                    ProgressBar(context).apply {
                        isIndeterminate = true
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            indeterminateTintList = ColorStateList.valueOf(Color.WHITE)
                        }
                    }.lparams(width = 24.dp, height = 24.dp) {
                        gravity = Gravity.CENTER
                    }.also { addView(it) }

                    tvMessage = TextView(context).apply {
                        setTextColor(Color.WHITE)
                        textSize = 14F
                    }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
                        gravity = Gravity.CENTER
                        leftMargin = 8.dp
                        rightMargin = 8.dp
                    }.also { addView(it) }

                }.lparams(width = layoutWrapContent, height = layoutWrapContent)
                    .also { addView(it) }

            }.lparams(width = layoutWrapContent, height = layoutWrapContent) {
                gravity = Gravity.CENTER
            }.also { addView(it) }
        }
    }


    init {
        setContentView(progressLayoutView)

        // 获取对话框当前的参数值
        window?.attributes = window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = context.screenHeight - context.statusBarHeight
            gravity = Gravity.CENTER;
        }
        setCanceledOnTouchOutside(false)//击屏幕，dialog不消失；点击物理返回键dialog消失
    }

    fun showDialog(message: String?) {
        if (isShowing) return
        tvMessage.text = message
        show()
    }

    fun hideDialog() {
        if (!isShowing) return
        dismiss()
    }

}