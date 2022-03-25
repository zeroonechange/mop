import sun.net.NetworkServer
import java.io.File
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class HighOrderFunc {
    //  在另一个module 就无法使用了
    internal fun getSome(): String {
        return "internal"
    }
}

// 单例 另一种写法
class SingleIns {
    companion object {
        private var instance: SingleIns? = null
            get() {
                return instance ?: SingleIns().also { instance = it }
            }

        fun get(): SingleIns {
            return instance!!
        }
    }
}

class SingleIns2 {

    companion object {
        @Volatile
        private var instance: SingleIns2? = null
    }

    fun getInstance(): SingleIns2 {
        return instance ?: synchronized(SingleIns2::class.java) {
            instance ?: SingleIns2().also { instance = it }
        }
    }
}


class Rectangle(val width: Int, val heighg: Int) {

    val area: Int
        get() = this.width * this.heighg

    var counter = 0
        set(value) {
            if (value > 0) {
                field = value  // 幕后属性
            } else {
                field = 0
            }
        }
}

// 延迟加载
class MyLateinit {
    lateinit var myHappy: String

    fun isLoad() {
        if (this::myHappy.isInitialized) {
            // 是否已经初始化完成了
        }
    }
}

// SAM    只有一个抽象方法的接口  单一抽象方法 single abstract method   回调的时候 用这个 比较好
fun interface Intercepeter {
    fun accept(i: Int): Boolean
}

val isEven = object : Intercepeter {
    override fun accept(i: Int): Boolean {
        return i % 2 == 0
    }
}

// lambda tricks
val isEven2 = Intercepeter { it % 2 == 0 }

class TT {
    fun setBack(lis: Intercepeter) {
        lis.accept(0)
    }
}

fun main1() {
    val tt = TT()
    val lis = Intercepeter { it % 2 == 1 }
    tt.setBack(lis)

}

// 别名   替代名称
//例如
typealias NodeSet = Set<Network.Node>
typealias FileTable<K> = MutableMap<K, MutableList<File>> // 常类型 别名
typealias MyHandler = (Int, String, Any) -> Unit   //函数别名
typealias Predicate<T> = (T) -> Boolean   // 函数 泛型别名

class Network {
    class Node {}
}


typealias NameTypeAlias = String

@JvmInline
value class NameInlineClass(val s: String)

fun acceptString(s: String) {}
fun acceptNameTyleAlias(n: NameTypeAlias) {}
fun acceptNameInlineClass(p: NameInlineClass) {}
fun aa() {
    val naAl: NameTypeAlias = ""
    val naInCl: NameInlineClass = NameInlineClass("")
    val st: String = ""
    acceptString(naAl)

    acceptNameTyleAlias(st)
    acceptNameInlineClass(naInCl)
}

// 匿名对象
fun ab() {
    val hello = object {
        val hi = "hello"
        val wo = "world"

        /* override fun toString(): String {
             return "$hi $wo"
         }*/
        override fun toString(): String = "$hi   $wo"
    }
    print(hello)
}

// 扩展函数
fun MutableList<Int>.swap1(id1: Int, id2: Int) {
    val tmp = this[id1]
    this[id1] = this[id2]
    this[id2] = tmp
}

fun ac() {
    val list = mutableListOf(1, 2, 3)
    list.swap1(0, 2)
    list.swap(0, 2)
}

fun <T> MutableList<T>.swap(id1: Int, id2: Int) {
    val tmp = this[id1]
    this[id1] = this[id2]
    this[id2] = tmp
}

// 扩展函数 是静态解析的
fun maina() {
    open class Shape
    class Rect : Shape()

    fun Shape.getname() = "shape"
    fun Rect.getname() = "rect"
    fun printClsName(s: Shape) {
        println(s.getname())
    }
    printClsName(Rect())
}

// 扩展 属性
val <T> List<T>.lastIdx: Int
    get() = size - 1

// 伴生对象  类内部的对象声明 companion
class Myclass {
    companion object Factory {
        fun create(): Myclass = Myclass()
    }
}

class MyClass {
    companion object {}
}

// 看起来像其他语言的静态成员  在运行时 仍然时真实对象的实例对象成员 还能实现接口
// 使用 @JvmStatic 注解  可以将伴生对象的成员生成为真正的静态方法和字段
fun mainb() {
    val instance = Myclass.create() //
    val x = MyClass.Companion
}

//对象表达式  使用他们的地方立即执行/初始化的  匿名内部类  main方法里面创建 hello
//对象声明    第一次被返回时 延迟初始化的     类似 View.OnclickListener
//伴生对象    类被加载时 java静态初始化器 语义相匹配   类似于静态成员


// 委托   get set 方法 修复 重写？
// 这是委托属性  还有个委托接口  类似于 多态+代理模式
// 还是看属性委托吧  三种  延迟属性  可观察属性 多属性存储映射
class example {
    var p: String by Delegate()   //语法是  val/var <属性名>: <属性类型> by <表达式>
}

class Delegate {
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        return "$thisRef, tks for delegate '${property.name}' to me"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("$value has been assigned to '${property.name}'.")
    }
}

fun mainc() {
    val ex = example()
    println(ex.p)
    ex.p = "*****"
    println(ex.p)
}

//标准委托  lazy   默认同步锁-synchronized  线程安全的话 可以不加锁
val lazyValue: String by lazy(LazyThreadSafetyMode.NONE) {
    println("computed!")
    "hello"
}

fun mainv() {
    println(lazyValue)
    println(lazyValue)
}

// 可观察属性
class User {
    var name: String by Delegates.observable("<no name>") { property, oldValue, newValue ->
        println("$oldValue -> $newValue")
    }
}

fun mainav() {
    val user = User()
    user.name = "first"
    user.name = "second"
}

//委托给另一个属性
var top: Int = 0

class withDelegate(val another: Int)
class MyCls(var member: Int, val anotherIns: withDelegate) {
    var delegateMember: Int by this::member
    var delegateTop: Int by ::top
    val delegateAnother: Int by anotherIns::another
}
var MyCls.extDelegated: Int by ::top
var MyCls.outDelegate: Int by ::top

// 向后兼容 重命名一个属性 注解旧的属性 委托实现
class MyCls2 {
    var newName: Int = 0
    @Deprecated("use 'newName' instead ", ReplaceWith("newName"))
    var oldName: Int by this::newName
}
fun mainab() {
    var cls = MyCls2()
    cls.oldName = 33
    println(cls.newName)
    println(cls.oldName)
}
// 类似与 json解析   委托实现属性映射
class User1(val map: Map<String, Any?>){
    val name: String by map
    val age : Int by map
}

class User2(var map: MutableMap<String, Any?>){
    val name: String by map
    val age : Int by map
}

fun mainam() {
//    val user = User1(mapOf("name" to "John", "age" to 22))
    val user = User2(mutableMapOf("name" to "John", "age" to 22))
    println(user.name)
    println(user.age)
}

// 局部委托属性






























