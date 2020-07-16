package com.bugrui.jetpackproject.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.ext.screenHeight
import com.bugrui.jetpackproject.ext.statusBarHeight

/**
 * @Author: BugRui
 * @CreateDate: 2019/7/18 16:33
 * @Description: DialogFragment基类
 */
abstract class BaseDialogFragment : DialogFragment() {


    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val mDismissed = this::class.java.superclass?.getDeclaredField("mDismissed")
            val mShownByMe = this::class.java.superclass?.getDeclaredField("mShownByMe")
            mDismissed?.isAccessible = true
            mShownByMe?.isAccessible = true
            mDismissed?.setBoolean(this, false)
            mShownByMe?.setBoolean(this, true)
        } catch (e: Exception) {
        }

        val ft = manager.beginTransaction()
        if (isAdded) {
            ft.show(this)
        } else {
            ft.remove(this).add(this, tag)
        }
        ft.commitAllowingStateLoss()
    }

    open fun showDialog(manager: FragmentManager?) {
        if (!isAdded && !isVisible && !isRemoving) {
            show(manager!!, tag)
        }

    }

    override fun dismiss() {
        if (!isAdded) return
        super.dismissAllowingStateLoss()
    }

    @get:LayoutRes
    protected abstract val layoutRes: Int

    /**
     * 背景透明度(默认0.5f)
     */
    open fun dialogDimAmount(): Float = 0.5f


    /**
     * 弹窗宽度（默认填满）
     */
    open fun dialogWidth(): Int = WindowManager.LayoutParams.MATCH_PARENT


    /**
     * 弹窗高度（默认自适应）
     */
    open fun dialogHeight(): Int = WindowManager.LayoutParams.WRAP_CONTENT

    /**
     * 弹窗居于屏幕的位置（默认居中）
     */
    open fun dialogGravity(): Int = Gravity.CENTER


    /**
     * 弹窗出现动画
     */
    open fun dialogWindowAnimations(): Int = 0


    /**
     * 设置对话框可取消
     */
    open fun isDialogCancelable(): Boolean = true

    /**
     * 设置对话框可取消
     */
    fun setDialogCancelable(isCancel: Boolean) {
        dialog?.setCancelable(isCancel)
        dialog?.setCanceledOnTouchOutside(isCancel)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null && layoutRes != 0) {
            return inflater.inflate(layoutRes, container, false)
        }
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCancelable(isDialogCancelable())
        dialog?.setCanceledOnTouchOutside(isDialogCancelable())
        afterOnViewCreated(view, savedInstanceState)
    }

    abstract fun afterOnViewCreated(view: View, savedInstanceState: Bundle?)


    override fun onStart() {
        super.onStart()
        if (dialog == null) return
        val window = dialog!!.window ?: return
        val params = window.attributes ?: return
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        params.dimAmount = dialogDimAmount()
        params.width = dialogWidth()
        if (dialogHeight() == WindowManager.LayoutParams.MATCH_PARENT) {
            params.height = requireContext().screenHeight - requireContext().statusBarHeight
        } else {
            params.height = dialogHeight()
        }
        params.gravity = dialogGravity()
        params.windowAnimations = dialogWindowAnimations()
        window.attributes = params
    }


}