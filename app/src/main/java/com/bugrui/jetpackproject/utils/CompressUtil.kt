package com.bugrui.jetpackproject.utils

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.bugrui.jetpackproject.common.AppExecutors
import top.zibin.luban.Luban


object CompressUtil {

    fun compress(context: Context, imgPath: String): LiveData<CompressResult> {
        val liveData = MutableLiveData<CompressResult>()
        AppExecutors.get.diskIO().execute {
            try {
                val get = Luban.with(context)
                    .setTargetDir(FileUtil.getFileCachePath(context)).load(imgPath).get()
                get.forEach { liveData.postValue(CompressResult(1, "压缩成功", it.absolutePath)) }
            } catch (e: Exception) {
                e.printStackTrace()
                liveData.postValue(CompressResult(0, "图片压缩失败${e.message}", imgPath))
            }
        }
        return liveData
    }

}

data class CompressResult(
    var status: Int,//1为成功,0失败
    var msg: String? = null,
    var imgPath: String
) {
    val isSuccessful: Boolean
        get() = status == 1
}