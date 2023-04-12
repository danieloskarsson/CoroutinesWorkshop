package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.network.Order
import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

fun main() {
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
