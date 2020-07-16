package com.zyb56.common.utils.pdf

import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import com.bugrui.jetpackproject.base.BasicApp
import com.bugrui.jetpackproject.ext.toast
import com.bugrui.jetpackproject.utils.FileUtil
import com.tencent.smtt.sdk.TbsReaderView
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

class LoadPDFAsyncTask : AsyncTask<String?, Int?, File?> {
    private var onLoadPDFListener: LoadPDFListener? = null

    private val filePath = FileUtil.getFileCachePath(BasicApp.sApp) + File.separator + "pdf"

    private var fileName: String

    constructor(fileName: String) {
        this.fileName = fileName
    }

    constructor(fileName: String, loadPDFListener: LoadPDFListener) {
        this.fileName = fileName
        this.onLoadPDFListener = loadPDFListener
    }

    fun setOnLoadPDFListener(onLoadPDFListener: OnLoadPDFListener?) {
        this.onLoadPDFListener = onLoadPDFListener
    }

    /**
     * 这里是开始线程之前执行的,是在UI线程
     */
    override fun onPreExecute() {
        super.onPreExecute()
    }

    /**
     * 当任务被取消时回调
     */
    override fun onCancelled() {
        super.onCancelled()
        if (onLoadPDFListener != null) onLoadPDFListener!!.onFailureListener()
    }

    /**
     * 当任务被取消时回调
     */
    override fun onCancelled(file: File?) {
        super.onCancelled(file)
    }


    /**
     * 子线程中执行
     */
    override fun doInBackground(vararg params: String?): File? {
        val httpUrl = params[0]
        if (TextUtils.isEmpty(httpUrl)) {
            if (onLoadPDFListener != null) onLoadPDFListener!!.onFailureListener()
        }
        val pathFile = File(filePath)
        if (!pathFile.exists()) {
            pathFile.mkdirs()
        }
        val pdfFile = File(filePath + File.separator + fileName)
        if (pdfFile.exists()) {
            return pdfFile
        }
        try {
            val url = URL(httpUrl)
            val conn =
                url.openConnection() as HttpURLConnection
            conn.connectTimeout = 10000
            conn.readTimeout = 10000
            conn.requestMethod = "GET"
            conn.doInput = true
            conn.connect()
            val count = conn.contentLength //文件总大小 字节
            var curCount = 0 // 累计下载大小 字节
            var curRead = -1 // 每次读取大小 字节
            if (conn.responseCode == HttpURLConnection.HTTP_OK) {
                val `is` = conn.inputStream
                val fos = FileOutputStream(pdfFile)
                val buf = ByteArray(1024)
                while (`is`.read(buf).also { curRead = it } != -1) {
                    curCount += curRead
                    fos.write(buf, 0, curRead)
                    publishProgress(curCount, count)
                }
                fos.close()
                `is`.close()
                conn.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            if (onLoadPDFListener != null) onLoadPDFListener!!.onFailureListener()
            return null
        }
        return pdfFile
    }

    /**
     * 更新进度
     */
    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)
        if (onLoadPDFListener != null) {
            onLoadPDFListener!!.onProgressListener(values[0], values[1])
        }
    }


    /**
     * 当任务执行完成是调用,在UI线程
     */
    override fun onPostExecute(file: File?) {
        super.onPostExecute(file)
        if (onLoadPDFListener != null) {
            if (file != null && file.exists()) {
                onLoadPDFListener!!.onCompleteListener(file)
            } else {
                onLoadPDFListener!!.onFailureListener()
            }
        }
    }


    class OnLoadPDFListener(
        private val tbsReaderView: TbsReaderView,
        private val fileType: String
    ) : LoadPDFListener {

        override fun onCompleteListener(file: File?) {
            //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败

            val bsReaderTempFile = File(FileUtil.getFileCachePath(BasicApp.sApp))
            if (!bsReaderTempFile.exists()) bsReaderTempFile.mkdir()

            val bundle = Bundle()
            bundle.putString("filePath", file?.absolutePath)
            bundle.putString("tempPath", FileUtil.getFileCachePath(BasicApp.sApp))
            val result: Boolean = tbsReaderView.preOpen(fileType, false)
            if (result) {
                tbsReaderView.openFile(bundle)
            }
        }

        override fun onFailureListener() {
            toast("文件下载失败")
        }

        override fun onProgressListener(curPro: Int?, maxPro: Int?) {

        }
    }

    /**
     * 下载PDF文件时的回调接口
     */
    interface LoadPDFListener {
        /**
         * 下载完成
         */
        fun onCompleteListener(file: File?)

        /**
         * 下载失败
         */
        fun onFailureListener()

        /**
         * 下载进度 字节
         */
        fun onProgressListener(curPro: Int?, maxPro: Int?)
    }


}