package com.wp.android_base.model

import java.io.Serializable

/**
 *
 * Created by wp on 2019/9/11.
 *
 * Description:
 *
 */
class TokenItem : Serializable{
    var position:String = ""
    var address:String= ""
    var logo:String?=null
    var name:String= ""
    var symbol:String= ""
    var type:String= ""


    constructor(type:String,symbol:String,address:String,name:String){
        this.type = type
        this.symbol = symbol
        this.address = address
        this.name = name
    }

    override fun equals(other: Any?): Boolean {
        if(other === this){
            return true
        }
        if(other !is TokenItem){
            return false
        }
        return other.type == type && other.symbol == symbol && other.address == address
    }

    override fun hashCode(): Int {
        var result = 17
        result = result * 31 + type.hashCode()
        result = result * 31 + symbol.hashCode()
        result = result * 31 + address.hashCode()
        return result
    }

}