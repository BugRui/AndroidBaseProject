package com.bugrui.jetpackproject.utils

import android.content.Context
import android.content.SharedPreferences
import com.bugrui.jetpackproject.base.BasicApp
import java.lang.Exception
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

/**
 * SharedPreferences封装类SPUtils
 *
 * @author: Jerry
 * date:  On 2017/11/16.
 */
object SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    private const val FILE_NAME = "ZYBSharedPreferencesDB"

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param data
     */
    fun put(key: String?, data: Any) {
        val sp = BasicApp.sApp.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        if (data is String) {
            editor.putString(key, data)
        } else if (data is Int) {
            editor.putInt(key, data)
        } else if (data is Boolean) {
            editor.putBoolean(key, data)
        } else if (data is Float) {
            editor.putFloat(key, data)
        } else if (data is Long) {
            editor.putLong(key, data)
        } else {
            editor.putString(key, data.toString())
        }
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultValue
     * @return
     */
    fun get(key: String?, defaultValue: Any = ""): Any {
        val sp: SharedPreferences = BasicApp.sApp.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return try {
            when (defaultValue) {
                is String -> sp.getString(key, (defaultValue as String?))!!
                is Int -> sp.getInt(key, (defaultValue as Int?)!!)
                is Boolean -> sp.getBoolean(key, (defaultValue as Boolean?)!!)
                is Float -> sp.getFloat(key, (defaultValue as Float?)!!)
                is Long -> sp.getLong(key, (defaultValue as Long?)!!)
                else -> defaultValue
            }
        } catch (e: Exception) {
            defaultValue
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    fun remove(key: String?) {
        val sp: SharedPreferences = BasicApp.sApp.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.remove(key)
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        val sp: SharedPreferences = BasicApp.sApp.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        val editor = sp.edit()
        editor.clear()
        SharedPreferencesCompat.apply(editor)
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return true 有 false无
     */
    fun contains(key: String?): Boolean {
        val sp: SharedPreferences = BasicApp.sApp.getSharedPreferences(
            FILE_NAME,
            Context.MODE_PRIVATE
        )
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    val all: Map<String, *>
        get() {
            val sp: SharedPreferences = BasicApp.sApp.getSharedPreferences(
                FILE_NAME,
                Context.MODE_PRIVATE
            )
            return sp.all
        }

    /**
     * 创建一个解决SharedPreferencesCompat.apply方法的一个兼容类
     *
     * @author zhy
     */
    private object SharedPreferencesCompat {
        private val sApplyMethod =
            findApplyMethod()

        /**
         * 反射查找apply的方法
         *
         * @return
         */
        private fun findApplyMethod(): Method? {
            try {
                val clz: Class<*> = SharedPreferences.Editor::class.java
                return clz.getMethod("apply")
            } catch (e: NoSuchMethodException) {
            }
            return null
        }

        /**
         * 如果找到则使用apply执行，否则使用commit
         *
         * @param editor
         */
        fun apply(editor: SharedPreferences.Editor) {
            try {
                if (sApplyMethod != null) {
                    sApplyMethod.invoke(editor)
                    return
                }
            } catch (e: IllegalArgumentException) {
            } catch (e: IllegalAccessException) {
            } catch (e: InvocationTargetException) {
            }
            editor.commit()
        }
    }
}