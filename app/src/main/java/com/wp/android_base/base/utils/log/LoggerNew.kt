package com.wp.android_base.base.utils.log

import android.util.Log
import com.wp.android_base.BuildConfig

/**
 *
 * Created by wp on 2020/7/3.
 *
 * Description:
 *
 */
object LoggerNew {

    private const val Default_Tag = "android_base"

    //Logcat对单个日志输出限制为 4 * 1024个字符
    private const val MAX_LEN = 4000
    private const val TOP_BORDER = "╔═══════════════════════════════════════════════════════════════════════════════════════════════════"
    private const val LEFT_BORDER = "║ "
    private const val BOTTOM_BORDER = "╚═══════════════════════════════════════════════════════════════════════════════════════════════════"

    private val LINE_SEPARATOR = System.getProperty("line.separator")

    private const val NULL_TIPS = "Log with null object."
    private const val NULL = "null"
    private const val ARGS = "args"

    private val ENABLE_LOG = BuildConfig.DEBUG


    fun d(contents: Any?) {
        log(LoggerLevelDef.D, Default_Tag, contents)
    }

    fun d(tag: String, vararg contents: Any?) {
        log(LoggerLevelDef.D, tag, *contents)
    }

    fun e(contents: Any?) {
        log(LoggerLevelDef.E, Default_Tag, contents)
    }

    fun e(tag: String, vararg contents: Any?) {
        log(LoggerLevelDef.E, tag, *contents)
    }


    private fun log(logLevel: LoggerLevelDef, tag: String, vararg contents: Any?) {
        var tag: String? = tag
        if (!ENABLE_LOG) {
            return
        }
        val logContents = createLogContents(tag, contents)
        tag = logContents[0]
        val msg = logContents[1]
        printLog(logLevel, tag, msg)
    }

    private fun printLog(logLevel: LoggerLevelDef, tag: String, msg: String) {
        TODO("Not yet implemented")
    }

    private fun createLogContents(tag: String?, contents: Array<out Any?>): Array<String> {
        TODO("Not yet implemented")
    }


    enum class LoggerLevelDef{
        D,E
    }
}