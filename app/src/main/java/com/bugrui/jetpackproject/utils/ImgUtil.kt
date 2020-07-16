package com.bugrui.jetpackproject.utils

import android.text.TextUtils

object ImgUtil {

    /**
     * 获取网络图片名称
     * @param httpUrl 图片url
     * @return 图片名称
     */
     fun getImageName(httpUrl: String?): String {
        return if (TextUtils.isEmpty(httpUrl)) {
            ""
        } else {
            val spx =
                httpUrl!!.split("\\?".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
            val index = spx.lastIndexOf("/")
            try {
                spx.substring(index + 1, spx.length)
            } catch (e: Exception) {
                ""
            }

        }
    }
}