package com.bugrui.jetpackproject.ext

import android.text.TextUtils
import android.view.Gravity
import android.widget.Toast
import com.bugrui.jetpackproject.base.BasicApp
import es.dmoral.toasty.Toasty

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/10 16:56
 * @Description:       吐司
 */
fun toast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    if (TextUtils.isEmpty(text)) return
    val normal = Toasty.normal(BasicApp.sApp, text!!, duration)
    normal.setGravity(Gravity.CENTER, 0, 0)
    normal.show()
}
