package com.wp.android_base.test_kotlin.delegate

import com.wp.android_base.base.utils.log.Logger
import kotlin.reflect.KProperty

/**
 *
 * Created by wp on 2020/6/23.
 *
 * Description:标准委托
 *
 */
class Delegate {

    companion object{
        const val TAG = "Delegate"
    }

    /**
     * thisRef 必须与属性的所有者(@see TestDelegate的delegate的属性，那么这里的thisRef就是TestDelegate)相同或者是其超类型，这里用Any?，是所有类型的超类型
     */
    operator fun getValue(thisRef:Any?,property: KProperty<*>):String?{
        return "${property.name}:$thisRef"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String?){
        Logger.d("TAG","value = $value : $thisRef")
    }

}