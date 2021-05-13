package com.wp.android_base.test_kotlin.delegate

import com.wp.android_base.base.utils.log.Logger
import kotlin.properties.Delegates

/**
 *
 * Created by wp on 2020/6/23.
 *
 * Description:测试标准属性委托的，委托属性的拥有者
 *
 */
class DelegateOwner(map:Map<String,Any?>) {
    /**
     * 委托属性会从这个映射中取值（通过字符串键——属性的名称->delegateName,age）：
     */
    val delegateName:String? by map
    val age:Int? by map

    constructor():this(mutableMapOf())

    companion object{
        const val TAG = "DelegateOwner"
    }

    var delegate:String? by Delegate()


    /**
     * 可观察的属性委托，每次给name赋值，就会调用该lambda表达式里的处理程序
     */
    var name:String by Delegates.observable("init name"){
        property, oldValue, newValue ->
        Logger.d(TAG,"$property -> oldValue = $oldValue newValue = $newValue")
    }

    /**
     *  在属性被赋新值生效之前会调用传递给 vetoable 的处理程序
     */
    var name1:String by Delegates.vetoable("init name"){ property, oldValue, newValue ->
        Logger.d(TAG,"$property -> oldValue = $oldValue newValue = $newValue")
        return@vetoable "new name" != newValue
    }

}