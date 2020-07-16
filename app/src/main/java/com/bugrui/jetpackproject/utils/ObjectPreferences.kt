package com.bugrui.jetpackproject.utils

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Base64
import com.bugrui.jetpackproject.base.BasicApp
import java.io.*

/**
 * @Author:            BugRui
 * @CreateDate:        2019/12/9 17:36
 * @Description:       使用Preferences保存对象
 */
object ObjectPreferences {

    private const val NAME_OBJECT = "preferences_object"
    private val sharedPreferences: SharedPreferences
        get() = BasicApp.sApp.getSharedPreferences(
            NAME_OBJECT,
            Context.MODE_PRIVATE
        )

    /**
     * 通过SharedPreferences保存一个对象
     */
    fun <T> save(t: T, tClass: Class<T>): Boolean {
        val sp = sharedPreferences
        val editor = sp.edit()
        return try {
            val baos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(baos)
            oos.writeObject(t)
            val temp = String(Base64.encode(baos.toByteArray(), Base64.DEFAULT))
            editor.putString(tClass.simpleName, temp)
            editor.apply()
            true
        } catch (e: IOException) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 通过SharedPreferences获取保存的对象
     */
    fun <T> get(tClass: Class<T>): T? {
        val sp = sharedPreferences
        val temp = sp.getString(tClass.simpleName, "")
        return if (TextUtils.isEmpty(temp)) {
            null
        } else try {
            val bais =
                ByteArrayInputStream(Base64.decode(temp!!.toByteArray(), Base64.DEFAULT))
            val ois = ObjectInputStream(bais)
            ois.readObject() as T
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    /**
     * 通过SharedPreferences删除一个对象
     */
    fun <T> remove(tClass: Class<T>): Boolean {
        return try {
            val sp = sharedPreferences
            val editor = sp.edit()
            editor.remove(tClass.simpleName)
            editor.apply()
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    /**
     * 查询对象是否已经存在
     *
     * @return true 有 false无
     */
    fun <T> contains(tClass: Class<T>): Boolean {
        val obj: Any? = get(tClass)
        return obj != null
    }
}