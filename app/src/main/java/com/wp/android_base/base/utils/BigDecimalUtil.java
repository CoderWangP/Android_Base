package com.wp.android_base.base.utils;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * Created by wangpeng on 2018/7/26.
 *
 * 精度要求高的数据，最好以字符串形式存储，json对应实体属性也以String为类型，
 * 如果用基本数据类型，浮点数可能存储与真实值有差异,造成精度损失
 */

public class BigDecimalUtil {

    /**
     * 小数位数，默认4位
     */
    public static final int DEFAULT_SCALE = 4;

    /**
     * 以参与计算的数字的Scale作为小数位数，不做对齐处理
     */
    public static final int NO_SCALE = -1;

    /**
     * 舍入模式,默认四舍五入
     */
    public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;


    public static double roundToSignificantFigures(double num, int n) {
        if(num == 0) {
            return 0;
        }
        final double d = Math.ceil(Math.log10(num < 0 ? -num: num));
        final int power = n - (int) d;

        final double magnitude = Math.pow(10, power);
        final long shifted = Math.round(num*magnitude);
        return shifted/magnitude;
    }


    /**
     *
     * @param value
     * @param scale:法币两位小数
     * @return
     */
    public static String formatLegal(String value, int scale) {
        value = safeValue(value);
        if(compare(value,"1") < 0){
            //小于1
            String temp = value;
            int valueScale = getScale(value);
            int count = 0;
            for(int i=0;i<valueScale;i++){
                temp = mulNoScale(temp,"10");
                if(compare(temp,"1") >= 0){
                    break;
                }else{
                    count++;
                }
            }
            return setScale(value,count + scale);
        }else{
            return setScale(value,scale);
        }
    }


    public static String mulNoScale(String value1, String value2) {
        value1 = safeValue(value1);
        value2 = safeValue(value2);
        return new BigDecimal(value1).multiply(new BigDecimal(value2)).toPlainString();
    }

    /**
     * 设置精度
     *
     * @param value
     * @param scale
     * @return
     */
    public static String setScale(String value, int scale) {
        value = safeValue(value);
        return new BigDecimal(value).setScale(scale, ROUNDING_MODE).toPlainString();
    }

    /**
     * 获取小数位数
     *
     * @param value
     * @return
     */
    public static int getScale(String value) {
        value = safeValue(value);
        return new BigDecimal(value).scale();
    }


    /**
     * 加法
     * @param value1
     * @param value2
     * @return
     */
    public static String add(String value1,String value2){
        return add(value1,value2,NO_SCALE);
    }

    /**
     * 加法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static String add(String value1,String value2,int scale){
        value1 = safeValue(value1);
        value2 = safeValue(value2);

        if(scale == NO_SCALE){
            return new BigDecimal(value1).add(new BigDecimal(value2)).toPlainString();
        }
        return new BigDecimal(value1).add(new BigDecimal(value2)).setScale(scale,ROUNDING_MODE).toPlainString();
    }

    /**
     * 减法
     * @param value1
     * @param value2
     * @return
     */
    public static String sub(String value1,String value2){
        return sub(value1,value2,NO_SCALE);
    }

    /**
     * 减法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static String sub(String value1,String value2,int scale){
        value1 = safeValue(value1);
        value2 = safeValue(value2);

        if(scale == NO_SCALE){
            return new BigDecimal(value1).subtract(new BigDecimal(value2)).toPlainString();
        }
        return new BigDecimal(value1).subtract(new BigDecimal(value2)).setScale(scale,ROUNDING_MODE).toPlainString();
    }

    /**
     * 乘法
     * @param value1
     * @param value2
     * @return
     */
    public static String mul(String value1,String value2){
        return mul(value1,value2,NO_SCALE);
    }

    /**
     * 乘法
     * @param value1
     * @param value2
     * @param scale
     * @return
     */
    public static String mul(String value1,String value2,int scale){
        value1 = safeValue(value1);
        value2 = safeValue(value2);

        if(scale == NO_SCALE){
            return new BigDecimal(value1).multiply(new BigDecimal(value2)).toPlainString();
        }
        return new BigDecimal(value1).multiply(new BigDecimal(value2)).setScale(scale,ROUNDING_MODE).toPlainString();
    }


    /**
     * 除法
     * @param value1
     * @param value2 : value2为0时，默认直接返回value1的值
     * @return
     */
    public static String div(String value1,String value2){
        return div(value1,value2,DEFAULT_SCALE);
    }

    /**
     * 除法，如果除数为0，默认直接返回被除数
     * @param value1
     * @param value2
     * @param scale ：不带精度的话，如果除不尽的话，会抛出异常，最好带上精度
     * @return
     */
    public static String div(String value1,String value2,int scale){
        value1 = safeValue(value1);
        value2 = safeValue(value2);
        if(compareWithZero(value2) == 0){
            return value1;
        }
        return new BigDecimal(value1).divide(new BigDecimal(value2)).setScale(scale,ROUNDING_MODE).toPlainString();
    }

    /**
     * 与0比较
     * @param value
     * @return
     */
    public static int compareWithZero(String value){
        return compare(value,"0");
    }

    /**
     * 两个数比较大小
     * @param value1
     * @param value2
     * @return
     */
    public static int compare(String value1,String value2){
        value1 = safeValue(value1);
        value2 = safeValue(value2);
        return new BigDecimal(value1).compareTo(new BigDecimal(value2));
    }


    /**
     * 获取小数位数
     * @param value
     * @return
     */
    public static int scale(String value){
        value = safeValue(value);
        return new BigDecimal(value).scale();
    }


    /**
     * 值为空时，默认作0处理
     * @param value
     * @return
     */
    private static String safeValue(String value){
        if(isEmpty(value)){
            return "0";
        }
        return value;
    }


    /**
     * 判断值为空的情况
     * @param value
     * @return
     */
    private static boolean isEmpty(String value){
        return TextUtils.isEmpty(value) || "null".equalsIgnoreCase(value);
    }
}
