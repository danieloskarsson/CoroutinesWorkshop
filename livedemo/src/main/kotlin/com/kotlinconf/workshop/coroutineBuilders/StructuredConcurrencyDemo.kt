package com.kotlinconf.workshop.coroutineBuilders

import com.kotlinconf.workshop.util.log
import kotlinx.coroutines.*

fun main() {
    runBlocking {
        launch {
            delay(100)
            log("first child completes")
        }
        launch {
            delay(100)
            log("first child completes")
        }
    }
}
