package com.bugrui.jetpackproject.base

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.MotionEvent
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.bugrui.jetpackproject.R
import com.bugrui.jetpackproject.dialog.LoadingDialog
import com.bugrui.jetpackproject.ext.hideSoftKeyboard
import com.bugrui.jetpackproject.ext.isShouldHideKeyboard
import com.bugrui.jetpackproject.ext.showSoftKeyboard
import com.bugrui.jetpackproject.utils.StatusBarColorUtil


/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 10:10
 * @Description:       BaseAppCompatActivity
 */
abstract class BaseAppCompatActivity : AppCompatActivity() {

    private var loadingDialog: LoadingDialog? = null

    open val isOpenKeyboardControl: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT //设定为竖屏
        super.onCreate(savedInstanceState)
        StatusBarColorUtil.initStatusBar(this)
        setContentView(contentLayoutResId)
        activityOnCreate(savedInstanceState)
    }

    abstract val contentLayoutResId: Int

    abstract fun activityOnCreate(savedInstanceState: Bundle?)

    fun showLoading(message: String? = "请稍候…") {
        if (loadingDialog == null) {
            loadingDialog = LoadingDialog(this)
        }
        loadingDialog?.showDialog(message)
    }

    fun hideLoading() {
        loadingDialog?.hideDialog()
    }

    override fun onDestroy() {
        loadingDialog?.hideDialog()
        loadingDialog = null
        super.onDestroy()
    }

    /**
     * 点击EditText以外的地方隐藏键盘显示
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (isOpenKeyboardControl && ev.action == MotionEvent.ACTION_DOWN) {
            val editText = currentFocus
            if (editText is EditText) {
                when (isShouldHideKeyboard(editText, ev)) {
                    true -> editText.hideSoftKeyboard()
                    else -> editText.showSoftKeyboard()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

//    override fun startActivityForResult(intent: Intent?, requestCode: Int, options: Bundle?) {
//        super.startActivityForResult(intent, requestCode, options)
//        overridePendingTransition(
//            R.anim.activity_open_enter_animation,
//            R.anim.activity_open_exit_animation
//        )
//    }
//
//    override fun finish() {
//        super.finish()
//        overridePendingTransition(R.anim.activity_close_enter_animation, R.anim.activity_close_exit_animation);
//    }


}