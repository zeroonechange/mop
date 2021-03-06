//import java.io.File
//import javax.sql.DataSource
//import kotlin.properties.Delegates
//import kotlin.reflect.KProperty
//
//class HighOrderFunc {
//    //  在另一个module 就无法使用了
//    internal fun getSome(): String {
//        return "internal"
//    }
//}
//
//// 单例 另一种写法
//class SingleIns {
//    companion object {
//        private var instance: SingleIns? = null
//            get() {
//                return instance ?: SingleIns().also { instance = it }
//            }
//
//        fun get(): SingleIns {
//            return instance!!
//        }
//    }
//}
//
//class SingleIns2 {
//
//    companion object {
//        @Volatile
//        private var instance: SingleIns2? = null
//    }
//
//    fun getInstance(): SingleIns2 {
//        return instance ?: synchronized(SingleIns2::class.java) {
//            instance ?: SingleIns2().also { instance = it }
//        }
//    }
//}
//
//
//class Rectangle(val width: Int, val heighg: Int) {
//
//    val area: Int
//        get() = this.width * this.heighg
//
//    var counter = 0
//        set(value) {
//            if (value > 0) {
//                field = value  // 幕后属性
//            } else {
//                field = 0
//            }
//        }
//}
//
///**
// * lateinit 和  by lazy的区别
// * lateinit 用var 修饰  后面可以随意改   by lazy 只能用 val  不可改
// */
//
//// 延迟加载
//class MyLateinit {
//    lateinit var myHappy: String
//
//    fun isLoad() {
//        if (this::myHappy.isInitialized) {
//            // 是否已经初始化完成了
//        }
//    }
//}
//
//// SAM    只有一个抽象方法的接口  单一抽象方法 single abstract method   回调的时候 用这个 比较好
//fun interface Intercepeter {
//    fun accept(i: Int): Boolean
//}
//
//val isEven = object : Intercepeter {
//    override fun accept(i: Int): Boolean {
//        return i % 2 == 0
//    }
//}
//
//// lambda tricks
//val isEven2 = Intercepeter { it % 2 == 0 }
//
//class TT {
//    fun setBack(lis: Intercepeter) {
//        lis.accept(0)
//    }
//}
//
//fun main1() {
//    val tt = TT()
//    val lis = Intercepeter { it % 2 == 1 }
//    tt.setBack(lis)
//
//}
//
//// 别名   替代名称
////例如
//typealias NodeSet = Set<Network.Node>
//typealias FileTable<K> = MutableMap<K, MutableList<File>> // 常类型 别名
//typealias MyHandler = (Int, String, Any) -> Unit   //函数别名
//typealias Predicate<T> = (T) -> Boolean   // 函数 泛型别名
//
//class Network {
//    class Node {}
//}
//
//
//typealias NameTypeAlias = String
//
//@JvmInline
//value class NameInlineClass(val s: String)
//
//fun acceptString(s: String) {}
//fun acceptNameTyleAlias(n: NameTypeAlias) {}
//fun acceptNameInlineClass(p: NameInlineClass) {}
//fun aa() {
//    val naAl: NameTypeAlias = ""
//    val naInCl: NameInlineClass = NameInlineClass("")
//    val st: String = ""
//    acceptString(naAl)
//
//    acceptNameTyleAlias(st)
//    acceptNameInlineClass(naInCl)
//}
//
//// 匿名对象
//fun ab() {
//    val hello = object {
//        val hi = "hello"
//        val wo = "world"
//
//        /* override fun toString(): String {
//             return "$hi $wo"
//         }*/
//        override fun toString(): String = "$hi   $wo"
//    }
//    print(hello)
//}
//
//// 扩展函数
//fun MutableList<Int>.swap1(id1: Int, id2: Int) {
//    val tmp = this[id1]
//    this[id1] = this[id2]
//    this[id2] = tmp
//}
//
//fun ac() {
//    val list = mutableListOf(1, 2, 3)
//    list.swap1(0, 2)
//    list.swap(0, 2)
//}
//
//fun <T> MutableList<T>.swap(id1: Int, id2: Int) {
//    val tmp = this[id1]
//    this[id1] = this[id2]
//    this[id2] = tmp
//}
//
//// 扩展函数 是静态解析的
//fun maina() {
//    open class Shape
//    class Rect : Shape()
//
//    fun Shape.getname() = "shape"
//    fun Rect.getname() = "rect"
//    fun printClsName(s: Shape) {
//        println(s.getname())
//    }
//    printClsName(Rect())
//}
//
//// 扩展 属性
//val <T> List<T>.lastIdx: Int
//    get() = size - 1
//
//// 伴生对象  类内部的对象声明 companion
//class Myclass {
//    companion object Factory {
//        fun create(): Myclass = Myclass()
//    }
//}
//
//class MyClass {
//    companion object {}
//}
//
//// 看起来像其他语言的静态成员  在运行时 仍然时真实对象的实例对象成员 还能实现接口
//// 使用 @JvmStatic 注解  可以将伴生对象的成员生成为真正的静态方法和字段
//fun mainb() {
//    val instance = Myclass.create() //
//    val x = MyClass.Companion
//}
//
////对象表达式  使用他们的地方立即执行/初始化的  匿名内部类  main方法里面创建 hello
////对象声明    第一次被返回时 延迟初始化的     类似 View.OnclickListener
////伴生对象    类被加载时 java静态初始化器 语义相匹配   类似于静态成员
//
//// 委托？ by  延迟 bylazy  可观察 by observable  属性映射  by map
//// 干啥用  给别人 类似于中介  adapter
//
//// 委托   get set 方法 修复 重写？
//// 这是委托属性  还有个委托接口  类似于 多态+代理模式
//// 还是看属性委托吧  三种  延迟属性  可观察属性 多属性存储映射
//// 底层原理是 编译器 生成了一些 get和 set代码    还用了一些反射
//class example {
//    var p: String by Delegate()   //语法是  val/var <属性名>: <属性类型> by <表达式>
//}
//
//class Delegate {
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        return "$thisRef, tks for delegate '${property.name}' to me"
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        println("$value has been assigned to '${property.name}'.")
//    }
//}
//
//fun mainc() {
//    val ex = example()
//    println(ex.p)
//    ex.p = "*****"
//    println(ex.p)
//}
//
////标准委托  lazy   默认同步锁-synchronized  线程安全的话 可以不加锁
//val lazyValue: String by lazy(LazyThreadSafetyMode.NONE) {
//    println("computed!")
//    "hello"
//}
//
//fun mainv() {
//    println(lazyValue)
//    println(lazyValue)
//}
//
//// 可观察属性
//class User {
//    var name: String by Delegates.observable("<no name>") { property, oldValue, newValue ->
//        println("$oldValue -> $newValue")
//    }
//}
//
//fun mainav() {
//    val user = User()
//    user.name = "first"
//    user.name = "second"
//}
//
////委托给另一个属性
//var top: Int = 0
//
//class withDelegate(val another: Int)
//class MyCls(var member: Int, val anotherIns: withDelegate) {
//    var delegateMember: Int by this::member
//    var delegateTop: Int by ::top
//    val delegateAnother: Int by anotherIns::another
//}
//
//var MyCls.extDelegated: Int by ::top
//var MyCls.outDelegate: Int by ::top
//
//// 向后兼容 重命名一个属性 注解旧的属性 委托实现
//class MyCls2 {
//    var newName: Int = 0
//
//    @Deprecated("use 'newName' instead ", ReplaceWith("newName"))
//    var oldName: Int by this::newName
//}
//
//fun mainab() {
//    var cls = MyCls2()
//    cls.oldName = 33
//    println(cls.newName)
//    println(cls.oldName)
//}
//
//// 类似与 json解析   委托实现属性映射
//class User1(val map: Map<String, Any?>) {
//    val name: String by map
//    val age: Int by map
//}
//
//class User2(var map: MutableMap<String, Any?>) {
//    val name: String by map
//    val age: Int by map
//}
//
//fun mainam() {
////    val user = User1(mapOf("name" to "John", "age" to 22))
//    val user = User2(mutableMapOf("name" to "John", "age" to 22))
//    println(user.name)
//    println(user.age)
//}
//
//
//// 操作符重载  + *
//// +a  a.unaryPlus()
//// -a  a.unaryMinus()
//// !a  a.not()
//// a++ a.inc()
//// a-- a.dec()
//// a+b  a.plus(b)
//// a-b  a.miuns(b)
//// a*b  a.times(b)
//// a/b  a.div(b)
//// a%b  a.rem(b)
//// a..b   a.rangeTo(b)
////a in b  b.contains(a)
////a !in b !b.contains(a)
//data class Point(val x: Int, val y: Int)
//
//operator fun Point.unaryMinus() = Point(-x, -y)
//data class Counter(val dayIndex: Int) {
//    operator fun plus(increment: Int): Counter {
//        return Counter(dayIndex + increment)
//    }
//
//    operator fun times(std: String): Counter {
//        return Counter(dayIndex)
//    }
//}
//
//fun mainsz() {
//    val p = Point(10, 20)
//    print(-p)  // -10 -20
//
//    val co = Counter(30)
//    val another = co * "hello"
//    another.dayIndex
//}
//// invoke 操作符
////a()   a.invoke()
////a(i)  a.invoke(i)
////a(i,j)    a.invoke(i,j)
////a(i_1,......,i_n)    a.invoke(i_1,......,i_n)
//
//// 广义赋值
//// a+=b     a.plusAssign(b)
//// and so on ... like   a-=b  a*=b  a/=b  a%=b
//
//// 相等与不等表达式
//// a==b  a?.equals(b)?:(b==null)
//// a!=b  !(a?.equals(b)?:(b==null))
//
////比较操作符
//// a>b  a.compareTo(b)>0
//// and so on ... like a<b  a>=b  a<=b
//
//
////安全的调用  链式 返回 null
//fun mainzv() {
//    val a = "kotlin"
//    val b: String? = null
//    val c: String? = "null"
//    println(a?.length)
//    println(b?.length ?: 0.compareTo(12))
//    println(c?.length)
//
//    //elvis 操作符
//    val node: String? = null
//    val name = node ?: throw IllegalArgumentException("name excepted")
//    val fl = node as? Float // 安全转换
//
//    fun printTop() {
//        println("--top--")
//    }
//
//    class A {
//        fun printSon() {
//            println("--son--")
//        }
//
//        fun invokePrintSon(omitThis: Boolean = false) {
//            if (omitThis) printSon()
//            else this.printSon()
//        }
//    }
//    A().invokePrintSon()                 // son
//    A().invokePrintSon(omitThis = true)  // top
//}
//
//
//// 密封类
//sealed interface Error
//sealed class IOError() : Error
//class FieldReadError(val f: File) : IOError()
//class DatabaseError(val source: DataSource) : IOError()
//object RuntimeError : Error
//
//fun log(e: Error) = when (e) {
//    is FieldReadError -> {}
//    is DatabaseError -> {}
//    RuntimeError -> {}
//    else -> { }
//}
//
//
//enum class RGB { RED, GREEN, BLUE }
//
//inline fun <reified T : Enum<T>> printAll() {
//    print(enumValues<T>().joinToString { it.name })
//}
//
//fun mainXV() {
//    printAll<RGB>() // RED, GREEN, BLUE
//}
//
//
////内联类   解决包装类的内存分配问题   inline 已经被废弃了 过时了额
//@JvmInline
//value class PWD(private val s: String)
//
//val pwd = PWD("mymima123PWD") // 不存在 实例对象  在运行时 仅仅包含 String
//
//// 内联类可以声明函数和属性
//@JvmInline
//value class Name(val s: String) {
//    init {
//        require(s.length > 0) {}
//    }
//
//    val length: Int
//        get() = s.length
//
//    fun greet() {
//        println("hello, $s")
//    }
//}
//
//fun maincb() {
//    val name = Name("kk")
//    name.greet()  // greet 方法 会作为一个静态方法被调用
//    println(name.length) // 属性的 get 方法 会作为一个静态方法被调用
//}
//
////内联类的继承
//interface Printable {
//    fun preetyPrint(): String
//}
//
//@JvmInline
//value class NickName(val s: String) : Printable {
//    override fun preetyPrint(): String = "lets go $s"
//}
//
//fun mainCN() {
//    val nickName = NickName("cc")
//    println(nickName.preetyPrint()) //作为一个静态方法被调用
//}
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
