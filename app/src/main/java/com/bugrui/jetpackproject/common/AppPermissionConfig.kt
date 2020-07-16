package com.bugrui.jetpackproject.common

object AppPermissionConfig {

    /**
     * 读取手机状态权限
     */
    fun getReadPhoneStatePermission(): Array<String> {
        return arrayOf("android.permission.READ_PHONE_STATE")
    }

    /**
     * sdk卡权限
     */
    fun getStoragePermission(): Array<String> {
        return arrayOf(
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"
        )
    }

    /**
     * 相机权限
     */
    fun getCameraPermission(): Array<String> {
        return arrayOf("android.permission.CAMERA")
    }

    /**
     * 定位权限
     */
    fun getLocationPermission(): Array<String> {
        return arrayOf(
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"
        )
    }


}