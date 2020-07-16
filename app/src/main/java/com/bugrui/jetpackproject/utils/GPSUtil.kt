package com.bugrui.jetpackproject.utils

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.provider.Settings
import android.view.View

import androidx.appcompat.app.AppCompatActivity
import com.bugrui.jetpackproject.dialog.CommonUseDialog

/**
 * @Author: BugRui
 * @CreateDate: 2019/8/22 15:04
 * @Description: gps工具类
 */
object GPSUtil {

    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    fun isOPen(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        val gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        val network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gps || network

    }


//    /**
//     * 强制帮用户打开GPS
//     * @param context
//     */
//    fun openGPS(context: Context) {
//        val GPSIntent = Intent()
//        GPSIntent.setClassName(
//            "com.android.settings",
//            "com.android.settings.widget.SettingsAppWidgetProvider"
//        )
//        GPSIntent.addCategory("android.intent.category.ALTERNATIVE")
//        GPSIntent.data = Uri.parse("custom:3")
//        try {
//            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send()
//        } catch (e: PendingIntent.CanceledException) {
//            e.printStackTrace()
//        }
//    }

    fun openGPS(activity: AppCompatActivity) {
        if (isOPen(activity)) return
        CommonUseDialog.Builder(
            text = "没有开启定位",
            leftBtnText = "取消",
            rightBtnText = "去开启",
            rightBtnClickListener = View.OnClickListener {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                activity.startActivityForResult(intent, 887)
            }
        ).showDialog(activity.supportFragmentManager)
    }

}
