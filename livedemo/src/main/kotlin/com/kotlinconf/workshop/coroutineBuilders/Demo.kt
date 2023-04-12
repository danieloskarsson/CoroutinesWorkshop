package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.network.Order
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*
import kotlin.coroutines.coroutineContext

fun main() = runBlocking<Unit> {
    whichContextDoIHave()
    println("RunBlocking context: $coroutineContext")
    launch(Dispatchers.Default + CoroutineName("my")) {
        whichContextDoIHave()
        println("Launch parent context: $coroutineContext")
        launch(Dispatchers.IO) {
            whichContextDoIHave()
            println("Launch child context: $coroutineContext")
        }
    }
}

suspend fun whichContextDoIHave() {
//    launch() Does not work. Scope required.
    println(coroutineContext)
}

fun main2() {
    runBlocking(Dispatchers.Default) {
        val orders = loadOrders()
        log(orders)
    }
}

suspend fun loadOrders(): List<Order> = coroutineScope {
    val userId = async(CoroutineName("userId")) {
        service.login("user", "1234")
    }
    val shopInfo = async { service.loadShopInfo() }
    val orders = service.loadOrders(userId.await(), shopInfo.await())
    orders
}
