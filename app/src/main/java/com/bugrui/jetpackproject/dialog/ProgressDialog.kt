package com.bugrui.jetpackproject.dialog

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.base.BaseDialogFragment
import com.bugrui.jetpackproject.ext.dp
import com.bugrui.jetpackproject.ext.toColorInt
import com.bugrui.layout._LinearLayout
import com.bugrui.layout.layoutWrapContent

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 18:02
 * @Description:       加载过渡弹窗
 */
class ProgressDialog : BaseDialogFragment() {

    private var isCancel = false
    private var message: CharSequence? = null

    /**
     * 设置是否可以触摸外部取消(默认不可以)
     */
    fun setCanceledOnTouchOutside(isExternalCancel: Boolean) {
        this.isCancel = isExternalCancel
    }

    /**
     * 显示文案
     */
    fun setMessage(message: CharSequence?): ProgressDialog {
        this.message = message
        return this
    }

    override val layoutRes: Int = 0
    override fun dialogDimAmount(): Float = 0.1f
    override fun dialogWidth(): Int = WindowManager.LayoutParams.WRAP_CONTENT
    override fun dialogHeight(): Int = WindowManager.LayoutParams.WRAP_CONTENT
    override fun dialogWindowAnimations(): Int = R.style.UILoadingProgressDialog

    private var tvMessage: TextView? = null

    private val progressLayoutView by lazy {
        android.R.color.white.toColorInt
        1.toColorInt
        _LinearLayout(requireContext()).apply {
            gravity = Gravity.CENTER
            orientation = LinearLayout.VERTICAL
            lparams(width = layoutWrapContent, height = layoutWrapContent) {
                gravity = Gravity.CENTER
            }

            CardView(context).apply {
                setCardBackgroundColor(Color.parseColor("#B3363636"))
                radius = 6F
                cardElevation = 0F
                gravity = Gravity.CENTER
                _LinearLayout(requireContext()).apply {
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
                        text = "请稍候…"
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState) ?: progressLayoutView
    }

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.apply {
            setCancelable(isCancel)
            setCanceledOnTouchOutside(isCancel)
        }
        tvMessage?.text = when (TextUtils.isEmpty(message)) {
            true -> "请稍候…"
            else -> message
        }
    }

}