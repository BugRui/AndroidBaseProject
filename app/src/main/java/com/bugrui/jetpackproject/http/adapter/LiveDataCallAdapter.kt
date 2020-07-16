package com.bugrui.jetpackproject.http.adapter

import androidx.lifecycle.LiveData
import com.bugrui.jetpackproject.http.ApiResponse
import com.google.gson.JsonParseException
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataCallAdapter<R>(private val responseType: Type) :

    CallAdapter<ApiResponse<R>, LiveData<ApiResponse<R>>> {

    override fun responseType() = responseType

    override fun adapt(call: Call<ApiResponse<R>>): LiveData<ApiResponse<R>> {
        return object : LiveData<ApiResponse<R>>() {
            private var started = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<ApiResponse<R>> {

                        override fun onResponse(
                            call: Call<ApiResponse<R>>,
                            response: Response<ApiResponse<R>>
                        ) {
                            postValue(response.toApiResponse())
                        }

                        override fun onFailure(call: Call<ApiResponse<R>>, throwable: Throwable) {
                            postValue(ApiResponse.error(msg = throwable.apiErrorMessage()))
                        }
                    })
                }
            }
        }
    }
}

/**
 * 通过Response处理请求是否成功以及返回
 */
fun <R> Response<ApiResponse<R>>.toApiResponse(): ApiResponse<R> {

    //http请求body为空
    if (this.body() == null) {
        return ApiResponse.error(
            msg = this.message(),
            code = this.code()
        )
    }

    //http请求异常
    if (!this.isSuccessful) {
        return ApiResponse.error(
            msg = this.message(),
            data = this.body()!!.data,
            code = this.code()
        )
    }
    //接口请求不成功
    if (!this.body()!!.isSuccessful()) {
        return ApiResponse.error(
            msg = this.body()!!.msg,
            data = this.body()!!.data,
            code = this.body()!!.code
        )
    }
    //接口请求成功
    return ApiResponse.success(
        msg = this.body()!!.msg,
        data = this.body()!!.data,
        code = this.body()!!.code
    )
}

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/10 16:40
 * @Description:       java类作用描述
 */
fun Throwable.apiErrorMessage(): String {
    return when (this) {
        is JsonParseException -> "服务器数据异常，请重试！"
        is ConnectException -> "连接不到服务器，请稍后再试！"
        is TimeoutException -> "您的手机网络不太畅哦！"
        is SocketTimeoutException -> "您的手机网络不太畅哦！"
        is UnknownHostException -> "无网络~"
        is IOException -> "连接不到服务器，请稍后再试！"
        else -> "请求连接失败，请重试！"
    }
}

