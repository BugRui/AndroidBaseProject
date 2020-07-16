package com.bugrui.jetpackproject.base

import android.app.Application
import com.bugrui.jetpackproject.common.AppConfig
import com.bugrui.jetpackproject.http.factory.RetrofitConverterFactory
import com.bugrui.jetpackproject.utils.DeviceUtil
import com.bugrui.request.APIRequest
import com.bugrui.jetpackproject.http.factory.LiveDataCallAdapterFactory
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 10:13
 * @Description:       BaseApplication
 */
class BasicApp : Application() {

    override fun onCreate() {
        super.onCreate()
        sApp = this

        //日志拦截器
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = when (AppConfig.isDebug()) {
                true -> HttpLoggingInterceptor.Level.BODY
                else -> HttpLoggingInterceptor.Level.NONE
            }
        }

        APIRequest.init(
            this.applicationContext,
            "https://www.baidu.com"
        ) {
            okHttp {
                cache(Cache(File(this@BasicApp.cacheDir, "APIRequestCache"), 10 * 1024 * 1024L))
                connectTimeout(6 * 2, TimeUnit.SECONDS)    //连接默认超时时间(秒)
                writeTimeout(12 * 2, TimeUnit.SECONDS)     //连接默认写超时时间(秒)
                readTimeout(12 * 2, TimeUnit.SECONDS)      //连接默认读超时时间(秒)
                addNetworkInterceptor(loggingInterceptor)
            }
            retrofit {
                addConverterFactory(RetrofitConverterFactory.create())
                addCallAdapterFactory(LiveDataCallAdapterFactory())
            }
            //添加公共头
            addHeader("os", "android")
            addHeader("imei", DeviceUtil.getDeviceId(this@BasicApp))
            addHeader("version", AppConfig.getVersionName())
            addHeader("versionCode", AppConfig.getVersionCode().toString())
        }
    }

    companion object {
        @JvmStatic
        lateinit var sApp: BasicApp
            private set
    }
}