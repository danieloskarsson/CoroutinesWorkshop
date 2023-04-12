package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

fun main() {
    runBlocking(Dispatchers.Default) {
        val userId = async(CoroutineName("userId")) {
            service.login("user", "1234")
        }
        val shopInfo = async { service.loadShopInfo() }
        val orders = service.loadOrders(userId.await(), shopInfo.await())
        log(orders)
    }
}
