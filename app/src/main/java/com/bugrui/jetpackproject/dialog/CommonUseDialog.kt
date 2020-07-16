package com.bugrui.jetpackproject.dialog

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.fragment.app.FragmentManager
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.base.BaseDialogFragment
import kotlinx.android.synthetic.main.dialog_common_use.*

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/5 12:02
 * @Description: 常用对话框
 */
class CommonUseDialog() : BaseDialogFragment() {

    private var builder: Builder? = null

    fun setBuilder(builder: Builder): CommonUseDialog {
        this.builder = builder
        return this
    }

    override val layoutRes: Int =  R.layout.dialog_common_use
    override fun dialogDimAmount(): Float = 0.5f
    override fun dialogWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT
    override fun dialogHeight(): Int = WindowManager.LayoutParams.MATCH_PARENT
    override fun dialogGravity(): Int = Gravity.CENTER
    override fun dialogWindowAnimations(): Int = R.style.WindowScaleAnimation


    data class Builder(
        val isExternalCancel: Boolean = true,                                               //触摸外部取消
        val title: CharSequence = "温馨提示",                                                //标题
        val titleFontSize: Float = 16f,                                                     //标题字体大小
        @ColorInt val titleTextColor: Int = Color.parseColor("#333333"),         //标题字体颜色
        val showTitle: Boolean = false,                                                     //标题是否显示
        val text: CharSequence?,                                                            //内容
        val isTextCenter: Boolean = false,                                                  //是否内容文字居中
        val textFontSize: Float = 16f,                                                      //内容字体大小
        @ColorInt val textColor: Int = Color.parseColor("#333333"),              //内容文字字体颜色
        val isLeftBtnHide: Boolean = false,                                                 //是否隐藏左边按钮
        val leftBtnText: CharSequence = "取消",                                              //左边按钮文字
        @ColorInt val leftBtnTextColor: Int = Color.parseColor("#999999"),        //左边按钮文字字体颜色
        @DrawableRes val leftBtnBackgroundColor: Int = -1,                                   //左边按钮背景颜色
        val leftBtnTextFontSize: Float = 16f,                                                //左边按钮文字字体大小
        val leftBtnClickListener: View.OnClickListener? = null,                              //左边按钮点击监听
        val isRightBtnHide: Boolean = false,                                                 //是否隐藏右边按钮
        val rightBtnText: CharSequence = "确定",                                             //右边按钮文字
        @ColorInt val rightBtnTextColor: Int = Color.parseColor("#ff9624"),       //右边按钮文字字体颜色
        @DrawableRes val rightBtnBackgroundColor: Int = -1,                                  //右边按钮背景颜色
        val rightBtnTextFontSize: Float = 16f,                                               //右边按钮文字字体大小
        val rightBtnClickListener: View.OnClickListener? = null                              //右边按钮点击监听
    ) {

        fun showDialog(manager: FragmentManager?) {
            CommonUseDialog()
                .setBuilder(this)
                .showDialog(manager)
        }
    }

    override fun afterOnViewCreated(view: View, savedInstanceState: Bundle?) {

        builder?.apply {

            setDialogCancelable(isExternalCancel)

            if (isTextCenter) {
                tvText.gravity = Gravity.CENTER
            }

            tvTitle.visibility = if (showTitle) View.VISIBLE else View.GONE
            tvTitle.text = title
            tvTitle.textSize = titleFontSize
            tvTitle.setTextColor(titleTextColor)
            tvText.text = text
            tvLeftBtn.text = leftBtnText
            tvRightBtn.text = rightBtnText
            tvText.setTextColor(textColor)
            tvLeftBtn.setTextColor(leftBtnTextColor)
            tvRightBtn.setTextColor(rightBtnTextColor)
            tvText.textSize = textFontSize
            tvLeftBtn.textSize = leftBtnTextFontSize
            tvRightBtn.textSize = rightBtnTextFontSize

            if (leftBtnBackgroundColor != -1) {
                tvLeftBtn.setBackgroundResource(leftBtnBackgroundColor)
            }

            if (rightBtnBackgroundColor != -1) {
                tvRightBtn.setBackgroundResource(rightBtnBackgroundColor)
            }
            if (isLeftBtnHide) {
                tvLeftBtn.visibility = View.GONE
                vLineId.visibility = View.GONE
            }
            if (isRightBtnHide) {
                tvRightBtn.visibility = View.GONE
                vLineId.visibility = View.GONE
            }

            tvLeftBtn.setOnClickListener { v ->
                leftBtnClickListener?.onClick(v)
                dismiss()
            }
            tvRightBtn.setOnClickListener { v ->
                rightBtnClickListener?.onClick(v)
                dismiss()
            }
        }

    }
}