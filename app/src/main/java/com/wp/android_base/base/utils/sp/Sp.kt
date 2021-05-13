package com.wp.android_base.base.utils.sp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.wp.android_base.base.utils.AppModule

/**
 *
 * Created by wp on 2020/6/22.
 *
 * Description:
 *
 */
object Sp {

    const val DEFAULT = "default"

    fun from():SharedPreferences{
        return from(DEFAULT)
    }

    fun from(spName: String):SharedPreferences{
        return AppModule.provideContext().getSharedPreferences(spName,Context.MODE_PRIVATE)
    }

    /**
     * 读 SP 存储项  默认 sp file
     * @param key 键
     * @param default 默认值
     */
    inline fun <reified T> getValue(key: String, default: T): T = getValue(DEFAULT,key,default)

    /**
     * 读 SP 存储项
     * @param spName sp file
     * @param key 键
     * @param default 默认值
     */
    @Suppress("IMPLICIT_CAST_TO_ANY")
    inline fun <reified T> getValue(spName:String, key: String, default:T): T = with(from(spName)) {
        val res = when (default) {
            is Long -> getLong(key,default)
            is String -> getString(key, default) ?: ""
            is Int -> getInt(key, default)
            is Boolean -> getBoolean(key, default)
            is Float -> getFloat(key, default)
            else -> Gson().fromJson(getString(key,""),T::class.java)
        }
        res as T
    }

    /**
     * 写 SP 存储项  默认 sp file
     * @param key 键
     * @param default 默认值
     */
    inline fun <reified T> putValue(key: String, value: T) = putValue(DEFAULT,key,value)

    /**
     * 写 SP 存储项
     * @param spName sp file
     * @param key 键
     * @param default 默认值
     */
    inline fun <reified T> putValue(spName: String,key: String, value: T) = with(from(spName).edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> putString(key,Gson().toJson(value))
        }.apply()
    }
}