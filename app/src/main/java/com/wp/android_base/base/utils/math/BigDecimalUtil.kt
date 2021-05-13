package com.wp.android_base.base.utils.math

import android.text.TextUtils
import java.math.BigDecimal
import kotlin.math.ceil
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.roundToInt

/**
 * Created by wangpeng on 2018/7/26.
 *
 * 精度要求高的数据，最好以字符串形式存储，json对应实体属性也以String为类型，
 * 如果用基本数据类型，浮点数可能存储与真实值有差异,造成精度损失
 */
object BigDecimalUtil {
    /**
     * 小数位数，默认4位
     */
    private const val DEFAULT_SCALE = 4

    /**
     * 以参与计算的数字的Scale作为小数位数，不做对齐处理
     */
    private const val NO_SCALE = -1

    /**
     * 舍入模式,默认四舍五入
     */
    private const val ROUNDING_MODE = BigDecimal.ROUND_HALF_UP

    fun roundToSignificantFigures(num: Double, n: Int): Double {
        if (num == 0.0) {
            return 0.0
        }
        val d = ceil(log10(if (num < 0) -num else num))
        val power = n - d.toInt()
        val magnitude = 10.0.pow(power.toDouble())
        val shifted = (num * magnitude).roundToInt()
        return shifted / magnitude
    }

    /**
     *
     * @param value
     * @param scale:法币两位有效小数
     * @return
     */
    fun formatLegal(value: String?, scale: Int): String {
        val value1 = safeValue(value)
        return if (compare(value1, "1") < 0) {
            //小于1
            var temp = value1
            val valueScale = getScale(value1)
            var count = 0
            for (i in 0 until valueScale) {
                temp = mulNoScale(temp, "10")
                if (compare(temp, "1") >= 0) {
                    break
                } else {
                    count++
                }
            }
            setScale(value1, count + scale)
        } else {
            setScale(value1, scale)
        }
    }

    fun mulNoScale(value1: String?, value2: String?): String {
        val value1Safe = safeValue(value1)
        val value2Safe = safeValue(value2)
        return value1Safe.toBigDecimal().multiply(value2Safe.toBigDecimal()).toPlainString()
    }

    /**
     * 设置精度
     *
     * @param value
     * @param scale
     * @return
     */
    fun setScale(value: String?, scale: Int): String {
        var value = value
        value = safeValue(value)
        return BigDecimal(value).setScale(scale, ROUNDING_MODE).toPlainString()
    }

    /**
     * 获取小数位数
     *
     * @param value
     * @return
     */
    fun getScale(value: String?): Int {
        return safeValue(value).toBigDecimal().scale()
    }
    /**
     * 加法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    /**
     * 加法
     * @param value1
     * @param value2
     * @return
     */
    @JvmOverloads
    fun add(value1: String?, value2: String?, scale: Int = NO_SCALE): String {
        var value1 = value1
        var value2 = value2
        value1 = safeValue(value1)
        value2 = safeValue(value2)
        return if (scale == NO_SCALE) {
            BigDecimal(value1).add(BigDecimal(value2)).toPlainString()
        } else BigDecimal(value1).add(BigDecimal(value2)).setScale(scale, ROUNDING_MODE).toPlainString()
    }
    /**
     * 减法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    /**
     * 减法
     * @param value1
     * @param value2
     * @return
     */
    @JvmOverloads
    fun sub(value1: String?, value2: String?, scale: Int = NO_SCALE): String {
        var value1 = value1
        var value2 = value2
        value1 = safeValue(value1)
        value2 = safeValue(value2)
        return if (scale == NO_SCALE) {
            BigDecimal(value1).subtract(BigDecimal(value2)).toPlainString()
        } else BigDecimal(value1).subtract(BigDecimal(value2)).setScale(scale, ROUNDING_MODE).toPlainString()
    }
    /**
     * 乘法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    /**
     * 乘法
     * @param value1
     * @param value2
     * @return
     */
    @JvmOverloads
    fun mul(value1: String?, value2: String?, scale: Int = NO_SCALE): String {
        var value1 = value1
        var value2 = value2
        value1 = safeValue(value1)
        value2 = safeValue(value2)
        return if (scale == NO_SCALE) {
            BigDecimal(value1).multiply(BigDecimal(value2)).toPlainString()
        } else BigDecimal(value1).multiply(BigDecimal(value2)).setScale(scale, ROUNDING_MODE).toPlainString()
    }
    /**
     * 除法，如果除数为0，默认直接返回被除数
     * @param value1
     * @param value2
     * @param scale ：不带精度的话，如果除不尽的话，会抛出异常，最好带上精度
     * @return
     */
    /**
     * 除法
     * @param value1
     * @param value2 : value2为0时，默认直接返回value1的值
     * @return
     */
    @JvmOverloads
    fun div(value1: String?, value2: String?, scale: Int = DEFAULT_SCALE): String? {
        var value1 = value1
        var value2 = value2
        value1 = safeValue(value1)
        value2 = safeValue(value2)
        return if (compareWithZero(value2) == 0) {
            value1
        } else BigDecimal(value1).divide(BigDecimal(value2)).setScale(scale, ROUNDING_MODE).toPlainString()
    }

    /**
     * 与0比较
     * @param value
     * @return
     */
    fun compareWithZero(value: String?): Int {
        return compare(value, "0")
    }

    /**
     * 两个数比较大小
     * @param value1
     * @param value2
     * @return
     */
    fun compare(value1: String?, value2: String?): Int {
        var value1 = value1
        var value2 = value2
        value1 = safeValue(value1)
        value2 = safeValue(value2)
        return BigDecimal(value1).compareTo(BigDecimal(value2))
    }

    /**
     * 获取小数位数
     * @param value
     * @return
     */
    fun scale(value: String?): Int {
        var value = value
        value = safeValue(value)
        return BigDecimal(value).scale()
    }

    /**
     * 值为空时，默认作0处理
     * @param value
     * @return
     */
    private fun safeValue(value: String?): String {
        return if (value.isNullOrEmpty() || "null".equals(value, ignoreCase = true)) {
            "0"
        } else value
    }
}