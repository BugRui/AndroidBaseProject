package com.bugrui.jetpackproject.utils

import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.internal.`$Gson$Types`
import com.google.gson.reflect.TypeToken
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type


/**
 * @Author: BugRui
 * @CreateDate: 2019/7/18 16:33
 * @Description: Gson解析
 */
class JsonUtil {

    private var gson: Gson? = null

    companion object {
        @JvmStatic
        val get: JsonUtil by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { JsonUtil() }
    }

    init {
        gson = Gson()
    }

    fun getGson(): Gson = gson!!

    /**
     * 将object对象转成json字符串
     *
     * @param object
     * @return
     */
    fun toJson(`object`: Any) = gson?.toJson(`object`)

    /**
     * 将gsonString转成泛型bean
     *
     * @param jsonStr
     * @param cls
     * @return
     */
    fun <T> toBean(jsonStr: String, cls: Class<T>): T? {
        return gson?.fromJson(jsonStr, cls)
    }

    /**
     * 转成list
     * 泛型在编译期类型被擦除导致报错
     * @param jsonStr
     * @param cls
     * @return
     */
    fun <T> toList(jsonStr: String, cls: Class<T>): List<T>? {
        return gson?.fromJson(jsonStr, object : TypeToken<List<T>?>() {}.type)
    }

    /**
     * 转成list
     * 解决泛型问题
     * @param json
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> jsonToList(json: String, cls: Class<T>): List<T> {
        val gson = Gson()
        val list: MutableList<T> = ArrayList()
        val array = JsonParser().parse(json).asJsonArray
        for (elem in array) {
            list.add(gson.fromJson(elem, cls))
        }
        return list
    }

    /**
     * 转成list中有map的
     *
     * @param jsonStr
     * @return
     */
    fun <T> toListMaps(jsonStr: String?): List<Map<String, T>>? {
        return gson?.fromJson(jsonStr, object : TypeToken<List<Map<String?, T>?>?>() {}.type)
    }

    /**
     * 转成map的
     *
     * @param jsonStr
     * @return
     */
    fun toMaps(jsonStr: String?): HashMap<String, Any?>? {
        return gson?.fromJson<HashMap<String, Any?>>(
            jsonStr,
            object : TypeToken<HashMap<String, Any?>?>() {}.type
        )
    }

    fun beanToMap(requestParams: Any): Map<String, Any?>? {
        return toMaps(toJson(requestParams))
    }

    fun getSuperclassTypeParameter(subclass: Class<*>): Type {
        val superclass = subclass.genericSuperclass
        if (superclass is Class<*>) {
            throw RuntimeException("Missing type parameter.")
        }
        val parameterized = superclass as ParameterizedType?
        return `$Gson$Types`.canonicalize(parameterized!!.actualTypeArguments[0])
    }


}

