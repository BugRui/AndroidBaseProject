package com.bugrui.jetpackproject.utils

import android.util.Log
import com.bugrui.jetpackproject.common.AppConfig


class LogUtil {

    companion object {

        private val isDebug = AppConfig.isDebug()

        fun i(tag: String, msg: String) {

            var msg1 = msg

            if (isDebug) {
                return
            }
            val segmentSize = 3 * 1024
            //        int segmentSize = 65536;


            val length = msg1.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.i(tag, msg1 + "")
            } else {
                while (msg1.length > segmentSize) {// 循环分段打印日志
                    val logContent = msg1.substring(0, segmentSize)
                    msg1 = msg1.replace(logContent, "")
                    Log.i(tag, logContent + "")
                }
                Log.i(tag, msg1 + "")// 打印剩余日志
            }
        }

        fun e(tag: String?, msg: String?) {
            var msg1 = msg

            if (isDebug) {
                return
            }
            if (tag == null || tag.isEmpty() || msg1 == null || msg1.isEmpty()) {
                return
            }
            val segmentSize = 3 * 1024
            val length = msg1.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.e(tag, msg1 + "")
            } else {
                while (msg1!!.length > segmentSize) {// 循环分段打印日志
                    val logContent = msg1.substring(0, segmentSize)
                    msg1 = msg1.replace(logContent, "")
                    Log.e(tag, logContent + "")
                }
                Log.e(tag, msg1 + "")// 打印剩余日志
            }
        }

        fun d(tag: String?, msg: String?) {
            var msg1 = msg
            if (isDebug) {
                return
            }
            if (tag == null || tag.isEmpty() || msg1 == null || msg1.isEmpty()) {
                return
            }
            val segmentSize = 3 * 1024
            val length = msg1.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.d(tag, msg1 + "")
            } else {
                while (msg1!!.length > segmentSize) {// 循环分段打印日志
                    val logContent = msg1.substring(0, segmentSize)
                    msg1 = msg1.replace(logContent, "")
                    Log.d(tag, logContent + "")
                }
                Log.d(tag, msg1 + "")// 打印剩余日志
            }
        }

        fun v(tag: String?, msg: String?) {
            var msg1 = msg
            if (isDebug) {
                return
            }
            if (tag == null || tag.isEmpty() || msg1 == null || msg1.isEmpty()) {
                return
            }
            val segmentSize = 3 * 1024
            val length = msg1.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.v(tag, msg1 + "")
            } else {
                while (msg1!!.length > segmentSize) {// 循环分段打印日志
                    val logContent = msg1.substring(0, segmentSize)
                    msg1 = msg1.replace(logContent, "")
                    Log.v(tag, logContent + "")
                }
                Log.v(tag, msg1 + "")// 打印剩余日志
            }
        }

        fun w(tag: String?, msg: String?) {
            var msg1 = msg
            if (isDebug) {
                return
            }
            if (tag == null || tag.isEmpty() || msg1 == null || msg1.isEmpty()) {
                return
            }
            val segmentSize = 3 * 1024
            val length = msg1.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.w(tag, msg1 + "")
            } else {
                while (msg1!!.length > segmentSize) {// 循环分段打印日志
                    val logContent = msg1.substring(0, segmentSize)
                    msg1 = msg1.replace(logContent, "")
                    Log.w(tag, logContent + "")
                }
                Log.w(tag, msg1 + "")// 打印剩余日志
            }
        }

        fun wtf(tag: String?, msg: String?) {
            var m = msg
            if (isDebug) {
                return
            }
            if (tag == null || tag.isEmpty() || m == null || m.isEmpty()) {
                return
            }
            val segmentSize = 3 * 1024
            val length = m.length.toLong()
            if (length <= segmentSize) {// 长度小于等于限制直接打印
                Log.wtf(tag, m + "")
            } else {
                while (m!!.length > segmentSize) {// 循环分段打印日志
                    val logContent = m.substring(0, segmentSize)
                    m = m.replace(logContent, "")
                    Log.wtf(tag, logContent + "")
                }
                Log.wtf(tag, m + "")// 打印剩余日志
            }
        }
    }
}
