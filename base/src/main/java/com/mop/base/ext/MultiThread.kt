package com.mop.base.ext

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.atomic.AtomicInteger
import kotlin.random.Random
import kotlin.system.measureTimeMillis

class MultiThread {}


fun main21() = runBlocking {

}

/**
val channel = Channel<Int>() launch{ channel.send(x) }  -> launch{ channel.receive()  channel.close()  }
ReceiveChannel<Int> = produce<Int> { }

interface Channel<E> : SendChannel<E>, ReceiveChannel<E>
CoroutineScope.produce() : ReceiveChannel<E> {}

val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0)
withTimeoutOrNull(50) { tickerChannel.receive() }

yield  join  joinAll  cancel   CoroutineScope  supervisorScope

val handler = CoroutineExceptionHandler { _, e ->      }
with(CoroutineScope(coroutineContext + supervisor)) { }

AtomicInteger().getAndIncrement()
Mutex().withLock {    }

val response = CompletableDeferred<Int>()
actor<CounterMsg> {  response.complete(counter) }
-> .send(IncCounter)
-> .send(GetCounter(response))
-> CompletableDeferred.await()

select{
produce<String> {}.onReceive{}
produce<Int> {}.onReceive{}
}

 */

/*
select 表达式  俩个发送  一个处理
学不会 有点难呢  后面再弄吧  高级通道使用教程
* */

fun CoroutineScope.asyncString(time: Int) = async {
    delay(time.toLong())
    "waited for $time ms"
}
fun CoroutineScope.asyncStringList(): List<Deferred<String>>{
    val random = Random(3)
    return List(12) {asyncString(random.nextInt(1000))} //尼玛的还能这么写
}

fun main() = runBlocking<Unit> {
    val list = asyncStringList()
    val result = select<String> {
        list.withIndex().forEach{ (index, deferred) ->
            deferred.onAwait{ answer->
                "deferred $index produced answer $answer"
            }
        }
    }
    println(result)
    val countAct = list.count { it.isActive }
    println("$countAct coroutines are still alive")
}



fun CoroutineScope.produceNums(sid: SendChannel<Int>) = produce<Int> {
    for(num in 1..10){
        delay(100)
        select<Unit> {
            onSend(num){ } //
            sid.onSend(num) {}
        }
    }
}
// 一下子发 主通道  忙不过来 select自动选择 副通道去发送了
fun mainb2() = runBlocking<Unit> {
    val side = Channel<Int>()
    launch {
        side.consumeEach { println("---side consume $it ---") }
    }
    produceNums(side).consumeEach {
        println("---consume $it ---")
        delay(250)
    }
    println("---done---")
    coroutineContext.cancelChildren()
}



fun CoroutineScope.fizz() = produce<String> {
    while(true){
        delay(300)
        send("fizz")
    }
}
fun CoroutineScope.buzz() = produce<String> {
    while(true){
        delay(500)
        send("buzz")
    }
}

fun mainb1() = runBlocking<Unit> {
    val fizz = fizz()
    val buzz = buzz()
    repeat(7){
        select<Unit> {
            fizz.onReceive{ println("---fizz---$it") }
            buzz.onReceive{ println("---buzz---$it") }
        }
    }
    coroutineContext.cancelChildren()
}


/*
共享的可变状态与并发
	同步访问共享的可变状态  独一无二的解决方案

	线程安全的数据结构   AtomicInteger.incrementAndGet() 适用于普通计数器、集合、队列和其他标准数据结构以及它们的基本操作。然而，它并不容易被扩展来应对复杂状态、或一些没有现成的线程安全实现的复杂操作。

	以细粒度限制线程  限制在单个线程中   例如只能在UI线程更新内容
	以粗粒度限制线程  在单个线程中并发多个协程

互斥
	synchronized 或者 ReentrantLock
	在kotlin中 替代品叫 Mutex: lock 和 unlock 方法     锁是细粒度的 会付出一些代价
	lock() 是个挂起函数  不会阻塞线程
	或者  withLock 扩展函数 mutex.lock(); try{...}finally{ mutex.unlock();}

	Actors
		由协程、 被限制并封装到该协程中的状态以及一个与其它协程通信的 通道组合而成的⼀个实体。
		一个简单的 actor 可以简单的写成一个函数， 但是一个拥有复杂状态的 actor 更适合由类来表示
		actor协程构建器  通道 组合 发送  消息类-密封类
		actor 在高负载下比锁更有效，因为在这种情况下它总是有工作要做，而且根本不需要切换到不同的上下文
* */


sealed class CounterMsg   //  密封类  可以用来做类型  枚举那种的
object IncCounter : CounterMsg() // 递增计数器的单向消息
class GetCounter(val response: CompletableDeferred<Int>) : CounterMsg() //携带回复的信息

fun CoroutineScope.counterActor() = actor<CounterMsg> {
    var counter = 0 //状态
    for (msg in channel) { // 即将到来消息的迭代器
        when (msg) {
            is IncCounter -> counter++
            is GetCounter -> msg.response.complete(counter) // 类似回调
        }
    }
}

/**
 * 看源码注释  容易懂  mailbox channel sendChannel
 *
 * 只能靠发消息来执行操作  发消息 累加  发消息获取结果
 * actor是一个协程  协程是按顺序执行的  将状态限制到特定协程可以解决共享可变状态问题
 * 在高负载下比锁更有用  总是有工作要做  不要切换到上下文
 * 和 rabbitMQ一样的东西  队列
 *
 * actor 协程构建器是一个双重的 produce协程构建器
 * 一个actor与它接收消息的通道相关联 而一个producer与它发送元素的通道相关联
 */
fun mainv6() = runBlocking<Unit> {
    val counter = counterActor()
    withContext(Dispatchers.Default) {
        massiveRun {
            counter.send(IncCounter)
        }
    }
    val response = CompletableDeferred<Int>() // 发送一条消息 用来从一个 actor中获取数值
    counter.send(GetCounter(response))
    println("--- ${response.await()} ---")
    counter.close()
}


val mutex = Mutex()
var counter5 = 0
fun mainv5() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            mutex.withLock {
                counter5++
            }
        }
    }
    println("--- $counter5 ---")
}

fun mainv4() = runBlocking {
    withContext(Dispatchers.Default) {
        withContext(counterCxt) {  //不需要从Dispatchers.Default切到单线程上下文
            massiveRun {
                counter3++
            }
        }
    }
    println("--- $counter3 ---") // 刚好 100*1000
}

val counterCxt = newSingleThreadContext("CounterCxt")
var counter3 = 0
fun mainv3() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            withContext(counterCxt) {  //每次都需要从Dispatchers.Default切到单线程上下文
                counter3++
            }
        }
    }
    println("--- $counter3 ---") // 刚好 100*1000  但是耗时
}

// 原子操作  简单应用
var counter2 = AtomicInteger()
fun mainv2() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter2.getAndIncrement()
        }
    }
    println("--- $counter2 ---") // 刚好 100*1000
}

@Volatile
var counter1 = 0
fun mainv1() = runBlocking {
    withContext(Dispatchers.Default) {
        massiveRun {
            counter1++
        }
    }
    println("--- $counter1 ---")  // 88570   加了volidate是 94084
}

suspend fun massiveRun(action: suspend () -> Unit) {
    val n = 100
    val k = 1000
    val time = measureTimeMillis {
        coroutineScope {
            repeat(n) {
                launch { repeat(k) { action() } }
            }
        }
    }
    println("--- massiveRun finished ---")
}

/*
协程异常处理
    自动传播 launch  actor    未捕获异常 类似 Thread.uncaughtExceptionHandler
    用户暴露 async   produce  通过await或receive 来消费异常
CoroutineExceptionHandler  ==  GlobalScope.launch(handler) { }
取消与异常
    子协程异常  会取消其他子协程 最后取消父协程
    如果有多个异常  通常取第一个异常  同时绑定其他异常
    不想因为一个子协程失败而导致其他的全部取消 可用 SupervisorJob  取消只会向下传播
        这个异常处理有点特殊  在内部就设置了 CoroutineExceptionHandler
* */

fun mainc8() = runBlocking {
    val handler = CoroutineExceptionHandler { _, e ->
        println("--- 0 ---$e")
    }
    supervisorScope {
        val child = launch(handler) {
            println("--- 1 ---")
            throw AssertionError()
        }
        println("--- 2 ---")
    }
    println("--- 3 ---")  // 2 1 0 3
}

fun mainc7() = runBlocking {
    try {
        supervisorScope {
            val child = launch {
                try {
                    println("--- 0 ---")
                    delay(Long.MAX_VALUE)
                } finally {
                    println("--- 1 ---")
                }
            }
            yield()
            println("--- 2 ---")
            throw AssertionError()
        }
    } catch (e: AssertionError) {
        println("--- 3 ---$e")
    } // 0 2 1 3
}

fun mainc6() = runBlocking {
    val supervisor = SupervisorJob()
    with(CoroutineScope(coroutineContext + supervisor)) {
        val first = launch(CoroutineExceptionHandler { _, _ -> }) {
            println("--- 0 ---")
            throw AssertionError("---1---")
        }
        val second = launch {
//          first.join()
            println("--- 2 ---")
            try {
                delay(Long.MAX_VALUE)
            } finally {
                println("--- 3 ---")
            }
        }
        first.join()
        println("--- 4 ---")
        supervisor.cancel()
        second.join() // 0 2 4 3   为什么第二个还没join 就跑起来了 俩都不join  4 0 2  好家伙 自动run的
    }
}


fun mainc5() = runBlocking {
    val handler = CoroutineExceptionHandler { _, e ->
        println("---  $e --- ")
    }
    val job = GlobalScope.launch(handler) {
        val innder = launch {
            launch {
                launch {
                    throw IOException()
                }
            }
        }
        try {
            innder.join()
        } catch (e: CancellationException) {
            println("---0--")
            throw e
        }
    }
    job.join()
}

fun mainc4() = runBlocking {
    val handler = CoroutineExceptionHandler { _, e ->
        println("---  $e ---  ${e.suppressed[0]}")
    }
    val job = GlobalScope.launch(handler) {
        launch {
            try {
                delay(Long.MAX_VALUE)
            } finally {
                withContext(NonCancellable) {
                    println("---- 1 --- ")
                    delay(100)
                    println("---- 2 --- ")
                    throw IOException()
                }
            }
        }
        launch {
            delay(10)
            println("--- 3 ---")
            throw ArithmeticException()
        }
    }
    job.join()
}

fun mainc3() = runBlocking {
    val job = launch {
        val child = launch {
            try {
                println("--- 00 --- ")
                delay(Long.MAX_VALUE)
            } finally {
                println("--- 0 --- ")
            }
        }
        yield() // 让步  如果没有这个  主协程先执行  有这个 让子协程先跑
        println("--- 1 --- ")
        child.cancel()
//        child.join()  // 让child跑 如果不做其他 就阻塞在这里
//        yield()
        println("--- 2 --- ")
    }
    job.join()  //有没有是一个样 反正主协程都跑了
}

fun mainc2() = runBlocking {
    val handler = CoroutineExceptionHandler { _, e ->
        println("---- $e ----")
    }
    val job = GlobalScope.launch(handler) {
        throw AssertionError()
    }
    val deffer = GlobalScope.async(handler) {
        throw ArithmeticException()
    }
    joinAll(job, deffer)
}

fun mainc1() = runBlocking {
    val job = GlobalScope.launch {
        println("-------0------")
        throw IndexOutOfBoundsException()
    }
    job.join()
    println("-----1------")
    val deffer = GlobalScope.async {
        println("-----2-----")
        throw ArithmeticException()
    }
    try {
        deffer.await()
        println("-----3-----")
    } catch (e: Exception) {
        println("-----4-----")
    }
}

/*
 * yield  join  joinAll  cancel   CoroutineScope  supervisorScope
 */


/*
扇出  多个协程接受相同的管道  分布式工作  一个人发送是顺序的  多人接受那边是抢 无序  send ReceiveChannel
扇入  多个协程发送到同一个通道   SendChannel  receive  好像和普通的通道一样  未出现拥挤的情况

带缓冲的通道   capacity  缓冲区被占满时会引起阻塞
通道是公平的
计时器通道    会合  特定的延迟 产生unit  ticker
* */

//计时器通道
fun main9() = runBlocking {
    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0)
    var nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("-------0-------- $nextElement ")

    nextElement = withTimeoutOrNull(50) { tickerChannel.receive() }
    println("-------1-------- $nextElement ")

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("-------2-------- $nextElement ")

    println("######################")
    delay(150)

    nextElement = withTimeoutOrNull(1) { tickerChannel.receive() }
    println("-------3-------- $nextElement ")

    nextElement = withTimeoutOrNull(60) { tickerChannel.receive() }
    println("-------4-------- $nextElement ")

    tickerChannel.cancel()

}


data class Ball(var hits: Int)

suspend fun player(name: String, table: Channel<Ball>) {
    for (ball in table) {
        ball.hits++
        println("$name $ball")
        delay(300)
        table.send(ball)
    }
}

//通道是公平的
fun main8() = runBlocking {
    val table = Channel<Ball>()
    launch { player("ping", table) }
    launch { player("p0ng", table) }
    table.send(Ball(0))
    delay(1000)
    coroutineContext.cancelChildren()
}

// 带缓冲的通道
fun main7() = runBlocking {
    val channel = Channel<Int>(4)
    val sender = launch {
        repeat(10) {
            println("--before sending--- $it")
            channel.send(it)  // 前四个元素被加入到了缓冲区且发送 试图发射第五个时被挂起
        }
    }
    delay(1000)
    sender.cancel()
}

//扇入
suspend fun sendStr(channel: SendChannel<String>, s: String, time: Long) {
    while (true) {
        delay(time)
        channel.send(s)
    }
}

fun main6() = runBlocking {
    val channel = Channel<String>()
    launch { sendStr(channel, "father", 200L) }
    launch { sendStr(channel, "son", 500L) }
    repeat(6) { println(channel.receive()) }
    coroutineContext.cancelChildren()
}

// 扇出
fun CoroutineScope.produceNum() = produce<Int> {
    var x = 1
    while (true) {
        send(x++)
        delay(100)
    }
}

fun CoroutineScope.launchProcess(id: Int, channel: ReceiveChannel<Int>) = launch {
    for (msg in channel)
        println("processor #$id received $msg")
}

fun main5() = runBlocking {
    val producer = produceNum()
    repeat(5) { launchProcess(it, producer) }
    delay(950)
    producer.cancel()
}

/*
通道  Channel   使单个值在多个协程中相互传输   在流中传输值的方法  和 BlockingQueue 非常相似
    send 和 receive
    close 关闭指令 没有更多元素
    生产者-消费者
    produce - 协程构建器   配合 consumeEach 消费

管道  是一种一个协程在流中开始生产可能无穷多个元素的模式
    produce{ send } ->  ReceiveChannel{ send } -> ReceiveChannel.receive()
    素数例子 produce{ send } -> produce{ filter and send }

    通道和管道的区别：
        通道 channel 和 blockingueue 一样  send 和 receive
        管道 produce-send  可以 传递 Receivechannel-produce  类似于 map-filter 加多个处理方式
*/


//素数
fun CoroutineScope.numsfrom(start: Int) = produce<Int> {
    var x = start
    while (true) send(x++) // 无限发射
}

fun CoroutineScope.filter(numb: ReceiveChannel<Int>, prime: Int) = produce<Int> {
    for (x in numb) {
        if (x % prime != 0) send(x)  //过滤  这个numb难以理解
    }
}

fun main4() = runBlocking {
    var cur = numsfrom(2)
    repeat(10) {
        val prime = cur.receive()
        cur = filter(cur, prime)
    }
    coroutineContext.cancelChildren()
}

fun CoroutineScope.produceNums() = produce<Int> {
    var x = 1
    while (true) send(x++)
}

fun CoroutineScope.square(nums: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for (x in nums) send(x * x)
}

fun main3() = runBlocking {
    val nums = produceNums()
    val squares = square(nums)
    repeat(5) {
        println(squares.receive())
    }
    println("--done--")
    coroutineContext.cancelChildren()
}

fun main2() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x * x)
        channel.close()
    }
    repeat(6) { println(channel.receive()) } // close后还接受 会抛异常 ClosedReceiveChannelException
    for (y in channel) {
        println(y)
    } //没有close 会阻塞  用for来接受元素
    println("---done---")
}


fun main1() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for (x in 1..5) channel.send(x)
    }
    repeat(2) { println("qifenle" + channel.receive()) } //阻塞的 如果没有接收到的话
    repeat(3) { println("meiyoua" + channel.receive()) }
    println("---done---")
}