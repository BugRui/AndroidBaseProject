package com.bugrui.jetpackproject.utils

import android.content.Context
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.net.Uri
import android.os.Build
import android.os.Environment
import androidx.core.content.FileProvider
import com.bugrui.jetpackproject.base.BasicApp

import java.io.File
import java.io.IOException

/**
 * @Author: BugRui
 * @CreateDate: 2019/10/12 13:50
 * @Description: 文件工具类
 */
object FileUtil {

    /**
     * 缓存路径
     */
    fun getFileCachePath(context: Context, folderName: String = "zyb_data_caches"): String {
        val externalFilesDir = context.getExternalFilesDir(folderName)
        val recordDirectory = Environment.getExternalStorageDirectory()
            .absolutePath + "/${folderName}/"
        val fileDirPath =
            if (externalFilesDir == null) recordDirectory else externalFilesDir.absolutePath
        val recordDir = File(fileDirPath)

//        // 要保证目录存在，如果不存在则主动创建
//        if (!isAndroidQFileExists(context, recordDir, fileDirPath)) {
//            recordDir.mkdirs()
//        }
        if (!recordDir.exists()) {
            recordDir.mkdirs()
        }
        return recordDir.absolutePath
    }

    /**
     * 通过文件地址获取文件uri
     */
     fun getFilePathTransformUri(imageName: String): Uri {
        return if (Build.VERSION.SDK_INT >= 24) {
            FileProvider.getUriForFile(BasicApp.sApp, "com.zyb56.zyb.provider", File(imageName))
        } else {
            Uri.fromFile(File(imageName))
        }
    }


    /**
     * 判断公有目录文件是否存在，自Android Q开始，公有目录File API都失效，
     * 不能直接通过new File(path).exists();判断公有目录文件是否存在，正确方式如下：
     */
    fun isAndroidQFileExists(context: Context?, file: File, path: String): Boolean {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
//            return file.exists()
//        }
        if (context == null) {
            return false
        }
        var afd: AssetFileDescriptor? = null
        val cr = context.contentResolver
        try {
            val uri = Uri.parse(path)
            afd = cr.openAssetFileDescriptor(Uri.parse(path), "r")
            if (afd == null) {
                return false
            } else {
                afd.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        } finally {
            try {
                afd?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
        return true
    }


    //删除集合文件并通知相册更新
    fun deleteImageCacheFile(context: Context) {
        //删除
        if (getFileCachePath(context) == null) {
            return
        }
        val rootPath = getFileCachePath(context)
        val folder = File(rootPath)
        if (!folder.exists()) return
        val fa = folder.listFiles() ?: return
        for (f in fa) {
            //文件是否存在
            if (f.exists()) continue
            //执行删除
            val delete = f.delete()
            if (delete) {
                //删除成功，通知相册刷新
                val uri = Uri.fromFile(f)
                val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri)
                context.sendBroadcast(intent)
            }
        }
    }

    /**
     * 获取文件大小
     *
     * @param file
     * @return     
     */
    fun getPrintSize(file: File): String {
        var size = file.length()
        // 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return size.toString() + "B"
        } else {
            size = size / 1024
        }
        // 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
        // 因为还没有到达要使用另一个单位的时候
        // 接下去以此类推
        if (size < 1024) {
            return size.toString() + "KB"
        } else {
            size = size / 1024
        }
        if (size < 1024) {
            // 因为如果以MB为单位的话，要保留最后1位小数，
            // 因此，把此数乘以100之后再取余
            size = size * 100
            return (size / 100).toString() + "." + (size % 100).toString() + "MB"
        } else {
            // 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024
            return (size / 100).toString() + "." + (size % 100).toString() + "GB"
        }
    }
}
