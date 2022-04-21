package com.mop.base.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MultiThread {}



/*
通道  Channel   使单个值在多个协程中相互传输   在流中传输值的方法  和 BlockingQueue 非常相似
    send 和 receive
    close 关闭指令 没有更多元素
    生产者-消费者
    produce - 协程构建器   配合 consumeEach 消费

管道  是一种一个协程在流中开始生产可能无穷多个元素的模式
    produce{ send } ->  ReceiveChannel{ send } -> ReceiveChannel.receive()
    素数例子 produce{ send } -> produce{ filter and send }
*/

fun main21() = runBlocking {

}

fun CoroutineScope.produceNums() = produce<Int> {
    var x=1
    while(true) send(x++)
}
fun CoroutineScope.square(nums: ReceiveChannel<Int>): ReceiveChannel<Int> = produce {
    for(x in nums) send(x*x)
}

fun main() = runBlocking {
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
        for(x in 1..5) channel.send(x*x)
        channel.close()
    }
    repeat(6) { println(channel.receive()) } // close后还接受 会抛异常 ClosedReceiveChannelException
    for(y in channel){ println(y) } //没有close 会阻塞  用for来接受元素
    println("---done---")
}


fun main1() = runBlocking {
    val channel = Channel<Int>()
    launch {
        for(x in 1..5) channel.send(x)
    }
    repeat(2){ println("qifenle" + channel.receive()) } //阻塞的 如果没有接收到的话
    repeat(3){ println("meiyoua" + channel.receive()) }
    println("---done---")
}