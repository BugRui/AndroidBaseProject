package com.bugrui.jetpackproject.utils

import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bugrui.jetpackproject.utils.statusbar.FlymeStatusBarColorUtils
import com.bugrui.jetpackproject.utils.statusbar.MIUIStatusBarUtils
import java.util.*

object StatusBarColorUtil {

    fun initStatusBar(activity: AppCompatActivity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity.window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            when (Build.MANUFACTURER.toLowerCase(Locale.getDefault())) {
                "xiaomi" -> MIUIStatusBarUtils.MIUISetStatusBarLightMode(
                    activity,
                    true
                )
                "meizu" -> FlymeStatusBarColorUtils.setStatusBarDarkIcon(
                    activity,
                    true
                )
            }
            activity.window.statusBarColor = Color.TRANSPARENT
        } else {
            activity.window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }
    }

}