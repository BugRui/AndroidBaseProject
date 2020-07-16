package com.bugrui.jetpackproject.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.provider.Settings
import android.telephony.TelephonyManager
import android.text.format.Formatter
import com.bugrui.jetpackproject.base.BasicApp
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException
import java.security.MessageDigest
import java.util.*

/**
 * @Author: BugRui
 * @CreateDate: 2019/10/17 14:49
 * @Description: 设备相关
 */
object DeviceUtil {

    fun getDeviceInfo(versionName: String?): String {
        val fields = Build::class.java.fields
        val `object` = JSONObject()
        for (field in fields) {
            when (field.name) {
                "DISPLAY", "BRAND", "CPU_ABI", "CPU_ABI2", "IS_EMULATOR" -> try {
                    `object`.put(
                        String.format("%s", field.name.toLowerCase()),
                        String.format("%s", field[null])
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        try {
            `object`.put("vendor", Build.MANUFACTURER.toLowerCase())
            `object`.put("rom", totalInternalMemorySize)
            `object`.put("ram", getTotalMemory())
            `object`.put("appvs", versionName)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return `object`.toString()
    }

    /**
     * 获取手机内部总的存储空间
     *
     * @return
     */
    val totalInternalMemorySize: Long
        get() {
            val path = Environment.getDataDirectory()
            val stat = StatFs(path.path)
            val blockSize = stat.blockSizeLong
            val totalBlocks = stat.blockCountLong
            return totalBlocks * blockSize
        }// 读取meminfo第一行，系统总内存大小
    // 获得系统总内存，单位是KB，乘以1024转换为Byte
    // Byte转换为KB或者MB，内存大小规格化
// 系统内存信息文件

    /**
     * 获取总内存容量
     *
     * @return
     */
    fun getTotalMemory(): String {
        val str1 = "/proc/meminfo"// 系统内存信息文件
        val str2: String
        val arrayOfString: Array<String>
        var initial_memory: Long = 0

        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(
                localFileReader, 8192
            )
            str2 = localBufferedReader.readLine()// 读取meminfo第一行，系统总内存大小

            arrayOfString =
                str2.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            initial_memory =
                (Integer.valueOf(arrayOfString[1]) * 1024).toLong()// 获得系统总内存，单位是KB，乘以1024转换为Byte
            localBufferedReader.close()

        } catch (e: IOException) {
        }

        return Formatter.formatFileSize(
            BasicApp.sApp,
            initial_memory
        )// Byte转换为KB或者MB，内存大小规格化
    }

    /**
     * 获得设备硬件标识
     *
     * @param context 上下文
     * @return 设备硬件标识
     */
    fun getDeviceId(context: Context): String {
        val sbDeviceId = StringBuilder()
        //获得AndroidId（无需权限）
        val androidId = getAndroidId(context)
        //获得硬件uuid（根据硬件相关属性，生成uuid）（无需权限）
        val uuid = deviceUUID.replace("-", "")
        //追加androidId
        if (androidId.isNotEmpty()) {
            sbDeviceId.append(androidId)
            sbDeviceId.append("|")
        }
        //追加硬件uuid
        if (uuid.isNotEmpty()) {
            sbDeviceId.append(uuid)
        }
        //生成SHA1，统一DeviceId长度
        if (sbDeviceId.isNotEmpty()) {
            val hash = getHashByString(sbDeviceId.toString())
            val sha1 = bytesToHex(hash)
            //返回最终的DeviceId
            if (sha1.isNotEmpty()) return sha1
        }
        //如果以上硬件标识数据均无法获得，
        //则DeviceId默认使用系统随机数，这样保证DeviceId不为空
        return UUID.randomUUID().toString().replace("-", "")
    }

    //需要获得READ_PHONE_STATE权限，>=6.0，默认返回null
    @SuppressLint("HardwareIds", "MissingPermission")
    fun getIMEI(context: Context): String {
        try {
            val tm =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            return tm.deviceId
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    /**
     * 获得设备的AndroidId
     *
     * @param context 上下文
     * @return 设备的AndroidId
     */
    @SuppressLint("HardwareIds")
    private fun getAndroidId(context: Context): String {
        try {
            return Settings.Secure.getString(
                context.contentResolver,
                Settings.Secure.ANDROID_ID
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
        return ""
    }

    /**
     * 获得设备序列号（如：WTK7N16923005607）, 个别设备无法获取
     * @return 设备序列号
     */
    private val sERIAL: String
        @SuppressLint("HardwareIds")
        get() {
            try {
                return Build.SERIAL
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return ""
        }

    /**
     * 获得设备硬件uuid
     * 使用硬件信息，计算出一个随机数
     *
     * @return 设备硬件uuid
     */
    private val deviceUUID: String
        private get() = try {
            val dev =
                "3883756" + Build.BOARD.length % 10 + Build.BRAND.length % 10 +
                        Build.DEVICE.length % 10 + Build.HARDWARE.length % 10 +
                        Build.ID.length % 10 + Build.MODEL.length % 10 + Build.PRODUCT.length % 10
            //                    Build.SERIAL.length() % 10;
            UUID(
                dev.hashCode().toLong(),
                Build.DEVICE.hashCode().toLong()
            ).toString()
        } catch (ex: Exception) {
            ex.printStackTrace()
            ""
        }

    /**
     * 取SHA1
     * @param data 数据
     * @return 对应的hash值
     */
    private fun getHashByString(data: String): ByteArray {
        return try {
            val messageDigest = MessageDigest.getInstance("SHA1")
            messageDigest.reset()
            messageDigest.update(data.toByteArray(charset("UTF-8")))
            messageDigest.digest()
        } catch (e: Exception) {
            "".toByteArray()
        }
    }

    /**
     * 转16进制字符串
     * @param data 数据
     * @return 16进制字符串
     */
    private fun bytesToHex(data: ByteArray): String {

        val sb = StringBuilder()
        var stmp: String
        for (n in data.indices) {
            val v = data[n].toInt() and 0xFF
            stmp = Integer.toHexString(v)
            if (stmp.length == 1) sb.append("0")
            sb.append(stmp)
        }
        return sb.toString().toUpperCase(Locale.CHINA)
    }

}