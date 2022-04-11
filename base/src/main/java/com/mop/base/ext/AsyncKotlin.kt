import kotlinx.coroutines.*
import java.lang.ArithmeticException
import kotlin.system.measureTimeMillis

class AsyncKotlin {

}

// 异步程序设计    线程  回调 future，promise   反应式扩展   协程
// 线程需要昂贵的上下⽂切换  线程数受底层操作系统的限制
// 线程不容易使⽤。线程的 Debug，避免竞争条件是我们在多线程编程中遇到的常⻅问题
// 回调嵌套的难度 错误处理很复杂  回调地狱  写时爽
// 反应式扩展  RxJava  -可观察流
// 协程  可被挂起  ⼀种函数可以在某个时刻暂停执⾏并稍后恢复的想法   -  suspend
// 开发⼈员编写⾮阻塞代码与编写阻塞代码基本相同。  编程模型本身并没有真正改变

// 基础
// 取消与超时
// 组合挂起
// 上下文和调度器
// 异步流
// 通道
// 异常处理
// 共享的可变状态与并发
// select 实验性的



/*
命名协程以调试
自动分配的 id   可以自定义命名CoroutineName  - 看launch 可不可以用呢
用 + 操作符 指定一个调度器 dispatcher  和 一个 命名
launch(Dispatchers.Default + CoroutineName("test")) { }
协程作用域
生命周期 - CoroutineScope   抽象类  在activity销毁时 释放内存 避免内存泄露和溢出

将线程局部数据传递到协程与协程之间
ThreadLocal.asContextElement     高级的使用 -> ThreadContextElement
* */
/*fun main() = runBlocking<Unit> {

}*/


fun main() = runBlocking<Unit> {

}


// 模拟android中  activity的 生命周期
class Activity{
    private val mainScope = CoroutineScope(Dispatchers.Default)
    fun destroy(){
        mainScope.cancel()
    }
    fun doSth(){
        repeat(10) {i->
            mainScope.launch {
                delay((i+1)*200L)
                println("coroutine $i is done")
            }
        }
    }
}

fun main23() = runBlocking<Unit> {
    val act = Activity()
    act.doSth()
    println("-------1-------")
    delay(500)
    println("-------2-------")
    act.destroy()
    delay(1000)
}


inline fun getThreadName(): String = Thread.currentThread().name

fun log(msg: String) = println("current thread name is ${getThreadName()}  msg: $msg")

fun main22() = runBlocking<Unit>(CoroutineName("my-main")) {
    log("start main coroutine")
    val v1 = async(CoroutineName("v1 core")) {
        delay(500)
        log("complete v1")
        200
    }
    val v2 = async(CoroutineName("v2 core")) {
        delay(1000)
        log("complete v2")
        50
    }
    log("result is ${v1.await() + v2.await()}")
}

fun main21() = runBlocking<Unit> {
    launch(Dispatchers.Unconfined + CoroutineName("my-name-SpiderMan")) {
        println("Im working in ${getThreadName()}")
    }
}




/*
协程调度器
    确定了相关的协程在哪个线程或哪些线程上执行
    可以将协程限制在某个特定的线程执行，或将它分派到一个线程池，亦或是让它不受限地运行
    Dispatchers.Unconfined 是个特殊的调度器且似乎也运行在 main 线程中，但实际上， 它是一种不同的机制
    非受限调度器 有种虚拟内存的感觉  内存不够了  另外开辟了一个虚拟内存去应付

上下文
    coroutineContext [Job]
    CoroutineScope 中的 isActive 只是 coroutineContext[Job]?.isActive == true 的快捷方式

子协程
    当一个父协程被取消的时候，所有它的子协程也会被递归的取消。
    一个父协程总是等待所有的子协程执行结束
    request.join() // 等待请求的完成，包括其所有子协程
* */

fun main16() = runBlocking<Unit> {
    val request = launch {
        repeat(3) { i ->
            launch {
                delay((i + 1) * 200L)
                println("coroutine $i is done")
            }
        }
        println("wait my child to run ")
    }
    request.join()
    println("----complete----")
}


fun main15() = runBlocking<Unit> {
    val request = launch {
        launch(Job()) {
            println("job1: -----1-----")
            delay(1000)
            println("job1: -----2-----")
        }
        launch {
            delay(100)
            println("job2: -----1-----")
            delay(1000)
            println("job2: -----2-----")
        }
    }
    delay(500)
    request.cancel()
    delay(1000)
    println("main who has survived ?????? ")
}

fun main14() = runBlocking<Unit> {
    println("my job is  ${coroutineContext[Job]}")
    //  my job is  BlockingCoroutine{Active}@2328c243
}

fun main13() = runBlocking<Unit> {
    launch(Dispatchers.Unconfined) {
        val name = Thread.currentThread().name
        println("Unconfined : im working $name")
        delay(500)
        val name2 = Thread.currentThread().name
        println("Unconfined : after that, im working $name2")
    }

    launch {
        val name = Thread.currentThread().name
        println("mian : im working $name")
        delay(1000)
        val name2 = Thread.currentThread().name
        println("mian : after that, im working $name2")
    }
}


/*
协程:
计算型 不会被 cancelAndJoin()  实际工作中网络请求  怎么请求一半就取消?
CoroutineScope isActive 扩展属性  检查取消状态  感觉还是不太牢固 比如网络请求如何做呢
try - finally 释放资源
withContext(NonCancellable){ } 不可取消   例如操作文件 关闭通信通道等  可结合fiaally一起使用
withTimeout(15*1000L){}  超时   抛出 TimeoutCancellationException  是 CancellationException 子类  还能用try-catch 去做
async + await 并发 组合挂起
    launch返回Job
    而async返回Deferred-轻量级非阻塞future，稍后提供结果的promise，延期结果 await 也是一个Job 可以取消
    惰性-结果只有通过await获取的时候协程才启动 或Job在start调用的时候
    结构并发  如果子协程失败 其他子协程和父协程都会被取消掉
上下文调度器
    父协程上下文
    主线程
    默认调度器
    新的线程
*/

fun main12() = runBlocking<Unit> {
    launch {
        println("main  " + Thread.currentThread().name)
    }
    launch(Dispatchers.Unconfined) {  // 非受限调度器
        println("Unconfined  " + Thread.currentThread().name)
    }
    launch(Dispatchers.Default) {
        println("Default  " + Thread.currentThread().name)
    }
    launch(newSingleThreadContext("myOwnThread")) {
        println("myOwnThread  " + Thread.currentThread().name)
    }
}


fun main11() = runBlocking<Unit> {
    try {
        failedConcurrentSum()
    } catch (e: ArithmeticException) {
//        e.printStackTrace()
        println("compute failed with arithmetic exception")
    }
}

suspend fun failedConcurrentSum(): Int = coroutineScope {
    val one = async<Int> {
        try {
            delay(Long.MAX_VALUE)
            42
        } finally {
            println("first child was cancelled")
        }
    }
    val two = async<Int> {
        println("second child throws an exception")
        throw ArithmeticException()
    }
    one.await() + two.await()
}


// 这种 子协程发生了错误  其他子协程和父协程 都会取消
suspend fun concurrentSum(): Int = coroutineScope {
    val one = async { doSthOne() }
    val two = async { doSthTwo() }
    one.await() + two.await()
}

fun main10() = runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async(start = CoroutineStart.LAZY) { doSthOne() }
//        val one = async { doSthOne() }
        val two = async { doSthTwo() }
        println("the answer is ${one.await() + two.await()}")
    }
    println("total time cost $time")
}

suspend fun doSthOne(): Int {
    delay(1300L)
    return 1
}

suspend fun doSthTwo(): Int {
    delay(300L)
    return 5
}


var acquired = 0

class Resource {
    init {
        acquired++
    }

    fun close() {
        acquired--
    }
}

fun main9() {
    runBlocking {
        repeat(100_000) {
            launch {
                /* val resource = withTimeout(60){
                     delay(50)
                     Resource()
                 }
                 resource.close()*/
                var resource: Resource? = null
                try {
                    withTimeout(60) {
                        delay(50)
                        resource = Resource()
                    }
                } finally {
                    resource?.close()
                }
            }
        }
    }
    println(acquired)
}

fun main8() = runBlocking {
//    val result = withTimeout(1300L){
    val result = withTimeoutOrNull(1300L) {
        repeat(1000) { i ->
            println("im sleeping $i ...")
            delay(500L)
        }
        "Done"
    }
    println("result is $result")
}

fun main7() = runBlocking {
    val job = launch {
        try {
            repeat(1000) { i ->
                println("job: Im sleeping $i ... ")
                delay(500L)
            }
        } finally {
            withContext(NonCancellable) {
                println("job: Im running finally")
                delay(1000L)
                println("job: Im NonCancellable I've sleep for 1 second")
            }
        }
    }
    delay(1300L)
    println("main : I dont want waiting")
    job.cancelAndJoin()
    println("main: Now I can quit. ")
}

fun main6() = runBlocking {
    val st = System.currentTimeMillis()
    val job = launch(Dispatchers.Default) {
        var next = st
        var i = 0
        while (isActive) {
            if (System.currentTimeMillis() >= next) {
                println("job:Im sleeping ${i++} ... ")
                next += 500L
            }
        }
    }
    delay(1300L)
    println("main: Im tired of waiting")
    job.cancelAndJoin()
    println("main: Now, I can quit")
}


//取消
fun main5() = runBlocking {
    val job = launch {
        repeat(1000) { i ->
            println("job: Im sleeping $i ...")
            delay(500L)
        }
    }
    delay(1300L)
    println("main: Im tired of waiting!")
    job.cancel() // 取消作业
    job.join() // 等待作业执行结束
    println("main: Now I can quit.")

}

fun main4() = runBlocking {
    repeat(100_000) {  // 启动了100k个协程 5s后 输出一个点
        launch {
            delay(5000L)
            println(".")
        }
    }
}

fun main3() = runBlocking {
    val job = launch {
        delay(2000L)
        println("world!")
    }
    println("hello")
    job.join()  // wait until child coroutine complete
    println("---done---")
}


fun main2() = runBlocking {
    doWorld()
}

suspend fun doWorld() = coroutineScope {
    launch {
        delay(1000L)
        println("world")
    }
    println("hello")
}

fun main1() = runBlocking {
    launch {
        delay(1000L)
        println(" ** world")
    }
    println("hello -- ")
}

fun main0() {
    println("hello world")

    GlobalScope.launch {
        longTime()
    }
}

suspend fun longTime() {
    print("---long time ago---")
}