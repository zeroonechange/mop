package com.mop.base.ext

import kotlinx.coroutines.*
import kotlinx.coroutines.channels.*

class MultiThread {}


fun main21() = runBlocking {

}

/*
扇出  多个协程接受相同的管道  分布式工作  一个人发送是顺序的  多人接受那边是抢 无序  send ReceiveChannel
扇入  多个协程发送到同一个通道   SendChannel  receive  好像和普通的通道一样  未出现拥挤的情况

带缓冲的通道   capacity  缓冲区被占满时会引起阻塞
通道是公平的
计时器通道    会合  特定的延迟 产生unit  ticker
* */

//计时器通道
fun main() = runBlocking {
    val tickerChannel = ticker(delayMillis = 100, initialDelayMillis = 0)
    var nextElement = withTimeoutOrNull(1){ tickerChannel.receive() }
    println("-------0-------- $nextElement ")

    nextElement = withTimeoutOrNull(50){ tickerChannel.receive() }
    println("-------1-------- $nextElement ")

    nextElement = withTimeoutOrNull(60){ tickerChannel.receive() }
    println("-------2-------- $nextElement ")

    println("######################")
    delay(150)

    nextElement = withTimeoutOrNull(1){ tickerChannel.receive() }
    println("-------3-------- $nextElement ")

    nextElement = withTimeoutOrNull(60){ tickerChannel.receive() }
    println("-------4-------- $nextElement ")

    tickerChannel.cancel()

}


data class Ball(var hits: Int)
suspend fun player(name: String, table: Channel<Ball>){
    for(ball in table){
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
        repeat(10){
            println("--before sending--- $it")
            channel.send(it)  // 前四个元素被加入到了缓冲区且发送 试图发射第五个时被挂起
        }
    }
    delay(1000)
    sender.cancel()
}

//扇入
suspend fun sendStr(channel: SendChannel<String>, s: String, time: Long){
    while(true){
        delay(time)
        channel.send(s)
    }
}

fun main6() = runBlocking {
    val channel = Channel<String>()
    launch { sendStr(channel, "father", 200L) }
    launch { sendStr(channel, "son", 500L) }
    repeat(6){ println(channel.receive()) }
    coroutineContext.cancelChildren()
}

// 扇出
fun CoroutineScope.produceNum() = produce<Int> {
    var x = 1
    while(true) {
        send(x++)
        delay(100)
    }
}

fun CoroutineScope.launchProcess(id: Int, channel: ReceiveChannel<Int>) = launch {
    for(msg in channel)
        println("processor #$id received $msg")
}

fun main5() = runBlocking {
    val producer = produceNum()
    repeat(5){ launchProcess(it, producer) }
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
        通道 channel 和 blockingueue一样  send和receive
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
    repeat(10){
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