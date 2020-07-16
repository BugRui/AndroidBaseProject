package com.bugrui.jetpackproject.http.factory

import com.bugrui.jetpackproject.http.converter.RetrofitRequestBodyConverter
import com.bugrui.jetpackproject.http.converter.RetrofitResponseBodyConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

/**
 * @Author:            BugRui
 * @CreateDate:        2020/2/17 9:39
 * @Description:       转换工厂
 */
class RetrofitConverterFactory(val gson: Gson = Gson()) : Converter.Factory() {

    companion object {
        fun create(): RetrofitConverterFactory {
            return RetrofitConverterFactory(Gson())
        }
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<*, RequestBody>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return RetrofitRequestBodyConverter(gson, adapter)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val adapter = gson.getAdapter(TypeToken.get(type))
        return RetrofitResponseBodyConverter(gson, adapter)
    }

}