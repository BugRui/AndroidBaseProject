package com.bugrui.jetpackproject.pages.web.pdf

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.bugrui.jetpackproject.common.CommonActivityLifecycleBoundObserver
import com.bugrui.jetpackproject.base.BasicApp
import com.bugrui.jetpackproject.ext.toast
import com.bugrui.jetpackproject.utils.FileUtil
import com.tencent.smtt.sdk.TbsReaderView
import com.bugrui.jetpackproject.utils.generator.Md5FileNameGenerator
import com.zyb56.common.utils.pdf.LoadPDFAsyncTask
import java.io.File

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/27 11:41
 * @Description:       java类作用描述
 */
private val fileTypes = arrayOf("pdf", "ppt", "txt", "word", "excel")

class WebFileLoader(
    private val fragmentActivity: FragmentActivity
) : TbsReaderView.ReaderCallback {

    private val md5FileNameGenerator = Md5FileNameGenerator()
    private var tbsReaderView: TbsReaderView? = null

    /**
     * 如果是文件类型返回文件类型，如果不是返回null
     */
    fun getFileType(url: String): String {
        for (fileType in fileTypes) {
            if (url.contains(fileType)) {
                return fileType
            }
        }
        return "text"
    }


    /**
     * 是否为文件格式
     */
    fun isFileFormat(url: String): Boolean {
        for (fileType in fileTypes) {
            if (url.contains(fileType)) {
                return true
            }
        }
        return false
    }


    fun loadFile( webUrl: String): TbsReaderView? {
        val fileType = getFileType(webUrl)
        tbsReaderView = TbsReaderView(fragmentActivity, this)
        val fileName = md5FileNameGenerator.generate(
            System.currentTimeMillis().toString() + webUrl, fileType
        )
        LoadPDFAsyncTask(
            fileName,
            object : LoadPDFAsyncTask.LoadPDFListener {
                override fun onCompleteListener(file: File?) {
                    //增加下面一句解决没有TbsReaderTemp文件夹存在导致加载文件失败
                    val bundle = Bundle()
                    bundle.putString("filePath", file?.absolutePath)
                    bundle.putString(
                        "tempPath",
                        FileUtil.getFileCachePath(
                            BasicApp.sApp
                        )
                    )
                    val result = tbsReaderView?.preOpen(fileType, false) ?: false
                    if (result) {
                        tbsReaderView?.openFile(bundle)
                    }
                }

                override fun onFailureListener() {
                    toast("文件下载失败")
                }

                override fun onProgressListener(curPro: Int?, maxPro: Int?) {
                }

            }
        ).execute(webUrl)

        //当页面被销毁，自动回收tbsReaderView
        fragmentActivity.lifecycle.addObserver(object : CommonActivityLifecycleBoundObserver(fragmentActivity) {
            override fun onDestroy() {
                tbsReaderView?.onStop()
                tbsReaderView = null
            }
        })

        return tbsReaderView
    }

    override fun onCallBackAction(p0: Int?, p1: Any?, p2: Any?) {

    }


}