import kotlinx.coroutines.*

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


//取消
fun main() = runBlocking {
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