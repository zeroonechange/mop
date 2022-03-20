package com.mop.base.ext

import android.content.Context
import java.util.concurrent.locks.Lock


/**
 * 判断是否为空 并传入相关操作
 */
/*fun <T> Any?.notNull(f: () -> T, t: () -> T): T {
    return if (this != null) f() else t()
}*/

/**
 * 高阶函数 每一个函数都是对象 捕获一个闭包 会引入运行时间开销
 * 内联化 lambda 表达式可以消除这类的开销
 * 编译器没有为参数创建一个函数对象  而是生成代码  可能导致生成的代码增加
 */
//inline fun <T> lock(lock: Lock, body: () -> T): T = TODO()

inline fun <T> lock(lock: Lock, body: () -> T): T {
    return body.invoke()
}

// reified  泛型相关
// 修饰符来限定类型参数使其可以在函数内部访问它， 几乎就像是一个普通的类一样  泛型更泛了 泛型tricks
// reified关键字是用于Kotlin内联函数的,修饰内联函数的泛型,泛型被修饰后,在方法体里,能从泛型拿到泛型的Class对象
// 这与java是不同的,java需要泛型且需要泛型的Class类型时,是要把Class传过来的,但是kotlin不用了

inline fun <reified T> T?.notEmpey(notEmpty: () -> Unit, empty: () -> Unit) {
    if (this.toString().isNotEmpty()) {
        notEmpty.invoke()
    } else {
        empty.invoke()
    }
}

fun oridinary(block: () -> Unit){
    print("hi")
}

inline fun oridinary2(block: () -> Unit){
    print("hi")
}


fun test6(){
    oridinary {
        return@oridinary
    }

    oridinary2 {
        return
    }
}

class TreeNode {
    val parent: TreeNode? = null
    val value: Int = 0
}
// 不使用 reified
fun <T> TreeNode.findParentOfType(clazz: Class<T>): T? {
    var p = parent
    while (p != null && !clazz.isInstance(p)) {
        p = p.parent
    }
    @Suppress("UNCHECKED_CAST")
    return p as T?
}
// 使用 reified
inline fun <reified T> TreeNode.findParentOfType(): T? {
    var p = parent
    while (p != null && p !is T) {
        p = p.parent
    }
    return p as T?
}

fun test5() {
    var node = TreeNode()
    node.findParentOfType(TreeNode::class.java)
    node.findParentOfType<TreeNode>()
}

/**
 * 高阶函数与 lambda 表达式
 * Kotlin 函数都是头等的，这意味着它们可以存储在变量与数据结构中，并可以作为参数传给其他高阶函数以及从其他高阶函数返回。
 * 可以像操作任何其他非函数值一样对函数进行操作。
 * 为促成这点，作为一门静态类型编程语言的 Kotlin 使用一系列函数类型来表示函数并提供一组特定的语言结构，例如 lambda 表达式。
 */

//函数类型实例化
fun test3() {
//    val intF = String::toInt
//    {a,b -> a+b}
    val re = a(2)
    a.invoke(123)

    val stringPlus: (String, String) -> String = String::plus
    val intPlus: Int.(Int) -> Int = Int::plus

    stringPlus.invoke("<-", "->")
    stringPlus("Hello, ", "world!")

    intPlus.invoke(1, 1)
    intPlus(1, 2)
    2.intPlus(3) // 类扩展调用
}

val a = { i: Int -> i + 1 }
val sum: (Int, Int) -> Int = { x: Int, y: Int -> x + y }
val sum2 = { x: Int, y: Int -> x + y }

//实现函数类型接口的自定义类的实例
class TS : (Int) -> Int {
    override fun invoke(p1: Int): Int {
        TODO("Not yet implemented")
    }
}
// 将String 转换为 Int
class TS1 : (String) -> Int {
    override fun invoke(p1: String): Int {
        TODO("Not yet implemented")
    }
}

class IntTransformer : (Int) -> Int {
    override operator fun invoke(x: Int): Int = TODO()
}

val intFunction: (Int) -> Int = IntTransformer()

// 将函数作为参数传递  (R,T)->R
fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R,
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

//将代码块传递  不时执行
fun foo(a: Int, fuck: () -> Unit) {
    fuck.invoke()
}

fun test() {
    foo(1, fuck = {
        print("your mom bomb")
    })
    // 传递末尾的 lambda 表达式
    foo(1) {
        print("")
    }

    asList(1, 2, 3)
    asList<String>("a", "b", "c")
}

fun doub(a: Int) = 2 * a

//可变数量的参数
fun <T> asList(vararg ts: T): List<T> {
    var re = arrayListOf<T>()
    for (t in ts) {
        re.add(t)
    }
    return re
}

//中缀   俩个同类型的  就可以搞事情
infix fun Int.shl(x: Int): Int {
    return x * x + 23
}

fun test2() {
    val re = 1.shl(23)
    val re2 = 1 shl 3
}

/**
 * 设置适配器
 */
/*fun RecyclerView.setup(block: BindingAdapter.(RecyclerView) -> Unit): BindingAdapter {
    val adapter = BindingAdapter()
    adapter.block(this)
    this.adapter = adapter
    return adapter
}*/


/**
 * dp值转换为px
 */
fun Context.dp2px(dp: Int): Int {
    val scale = resources.displayMetrics.density
    return (dp * scale + 0.5f).toInt()
}

