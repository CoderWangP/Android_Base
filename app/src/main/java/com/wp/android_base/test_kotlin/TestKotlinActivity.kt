package com.wp.android_base.test_kotlin

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.TextView
import com.wp.android_base.R
import com.wp.android_base.base.BaseActivity
import com.wp.android_base.base.utils.log.Logger
import com.wp.android_base.test_kotlin.delegate.DelegateOwner
import kotlinx.android.synthetic.main.activity_kotlin.*
import java.lang.StringBuilder

/**
 *
 * Created by wp on 2020/3/18.
 *
 * Description:
 *
 * let run with apply also 总结:
 *
 * let lambda内 it指代自己，返回lambda最后一行执行结果
 * run lambda内 this上下文指代自己，与let，with一样，返回lambda最后一行执行结果
 * with with(T) 参数非空，不能执行空安全，this上下指代自己，与let run一样，返回lambda最后一行执行结果
 * apply lambda内 this上下文指代自己，与also一样返回自身
 * also lambda内 it指代自己，与apply一样，返回自身
 *
 *
 */
class TestKotlinActivity :BaseActivity(){

    companion object {
        const val TAG = "TestKotlinActivity"
        fun forward2TestKotlin(context: Context){
            val intent = Intent(context,TestKotlinActivity::class.java).apply {

            }
            context.startActivity(intent)
        }
    }

    override fun getContentLayoutId(): Int {
        return R.layout.activity_kotlin
    }

    override fun requestDatas() {
        super.requestDatas()
        Logger.d(TAG,"tx_test_kotlin.text() = ${tx_test_kotlin.text.length}")
        testInline()
    }

    private fun testInline(){
        tx_test_kotlin.setOnClickListener(object :View.OnClickListener{
            override fun onClick(v: View?) {

            }
        })
        tx_test_kotlin.setOnClickListener(){ v->
            val id = v.id
            Logger.e(TAG,"{ v->} -> id = $id")
        }

        tx_test_kotlin.setOnClickListener{
            val id = it.id
            Logger.e(TAG,"{it} -> id = $id")
        }

        tx_test_kotlin.addInlineTestInterface(object :InlineTestInterface{
            override fun onMethod1(v: View) {

            }

            override fun onMethod2(v: View) {

            }

        })

        testLet("testLet")
        testApply(StringBuilder("testApply"))
        testAlso(StringBuilder("testAlso"))
        testTakeIf(null)
        testTakeUnless("Unless")
        testRepeat(5)

        val testLambda1 = testLambda(3,{num1:Int,num2:Int ->num1 + num2})
        Logger.e(TAG,"testLambda1 = $testLambda1")
        //当函数最后一个参数是函数，并且传入lambda表达式作为相应参数，则可以在圆括号之外指定它
        val testLambda2 = testLambda(3){num1:Int,num2:Int ->num1 + num2}
        Logger.e(TAG,"testLambda2 = $testLambda2")

        //匿名函数
        val anonymous = fun(num1:Int,num2:Int):Int{
            return num1 + num2
        }
        Logger.e(TAG,"anonymous = ${anonymous(1,4)}" )

        //closure是个函数
        val closure = testClosure(3)
        Logger.e(TAG,"closure = $closure")
        val closure1 = closure.invoke()
        Logger.e(TAG,"closure1 = $closure1")

        //closure()相当于closure.invoke()，执行该方法，而且方法内部的局部变量状态被保存
        val closure2 = closure()
        Logger.e(TAG,"closure2 = $closure2")

        testRun()


        testDelegate()
    }

    private fun testDelegate() {
        val delegateOwner = DelegateOwner()
        delegateOwner.delegate = "测试Delegate"

        delegateOwner.name = "update name"

        delegateOwner.name1 = "update name"
        Logger.d(TAG,delegateOwner.name1)
        delegateOwner.name1 = "new name"
        Logger.d(TAG,delegateOwner.name1)

        val delegateOwner1 = DelegateOwner(mapOf("delegateName" to "delegater","age" to 20))
        Logger.d(TAG,"delegateOwner1.name = ${delegateOwner1.delegateName}","delegateOwner1.age = ${delegateOwner1.age}")
    }



    /**
     * run 函数 返回最后一行的执行结果，与apply不一样，apply返回自身
     */
    private fun testRun(){
        //返回了lambda表达式内执行的结果
        val length = run{
            "java"
            "kotlin"
        }.length
        Logger.e(TAG,"run length = $length")

        //返回最后一行的执行结果
        val result = "java".run {
            this.substring(1,this.length)
            this.length
        }
        Logger.e(TAG,"run result = $result")
    }


    /**
     * let匿名函数的最后一行是这个方法的返回值，可以返回不同的值，与also不一样，also返回的是自身
     */
    private fun testLet(testStr:String?){
        val result = testStr?.let {
            it.length
            1000
        }
        Logger.e(TAG,"test let result = $result")
    }

    /**
     * also与apply一样，{}执行之后，返回了自身对象，不同的是，在{}里只能用it来指代自身，而apply是this
     * 返回自身，即testStr
     */
    private fun testAlso(testStr: StringBuilder?){
        val result = testStr?.also {
            Logger.e(TAG,"also original = $it")
            it.reversed()
        }.also {
            Logger.e(TAG,"also str = $it")
            it?.length
        }
        Logger.e(TAG,"testAlso -> result = $result")

        val user = User().also {
            Logger.e(TAG,"also original name = ${it.name},age = ${it.age}")
            it.name = "user name"
        }.also {
            Logger.e(TAG,"also mid name = ${it.name},age = ${it.age}")
            it.age = 20
        }
        Logger.e(TAG,"also result name = ${user.name},age = ${user.age}")
    }

    /**
     * apply执行完了{}block函数后，返回了自身对象
     */
    private fun testApply(testStr: StringBuilder?){
        val result = testStr?.apply {
            Logger.e(TAG,"apply original = $this")
            reversed()
        }.apply {
            Logger.e(TAG,"testApply -> apply str = $this")
            this?.append(3)
            this?.length
        }
        Logger.e(TAG,"testApply -> result = $result")
    }

    /**
     * takeIf，如果{}函数条件成立则，返回自身，反之，则返回null
     */
    private fun testTakeIf(str:String?){
        val result = str?.takeIf {
            str.startsWith("ta")
        }
        Logger.e(TAG,"testTakeIf = $result")
    }

    /**
     * takeUnless,与takeIf相反，如果条件成立，返回null，反之，返回自身
     */
    private fun testTakeUnless(str: String?){
        val result = str?.takeUnless {
            str.startsWith("ta")
        }
        Logger.e(TAG,"result=$result")
    }

    /**
     * repeat重复某操作
     */
    private fun testRepeat(count:Int){
        repeat(count){
            Logger.e(TAG,"testRepeat index = $it")
        }
    }


    /**
     * lambda表达式作为函数参数，调用此函数时，需要为该lambda表达式写出它的具体实现
     */
    private fun testLambda(a:Int,b:(num1:Int,num2:Int) ->Int):Int{
        return a + b.invoke(3,4)
    }

   private fun testClosure(b:Int):() -> Int{
        var a = 3
        return fun():Int{
            a++
            return a + b
        }
    }



    interface InlineTestInterface{
        fun onMethod1(v:View)
        fun onMethod2(v:View)
    }


    private fun TextView.addInlineTestInterface(inlineTestInterface: InlineTestInterface){
        Logger.e(TAG,"addInlineTestInterface -> v.id = ")
    }
}