package com.kotlinconf.workshop.puzzlers

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val job = launch {
        var x = 0
        repeat(100) {
            x++
        }
        delay(500)
        foo()
        bar()
    }
    delay(500)
    job.cancel()
}

suspend fun bar() {
    delay(500)
    println("Bar executed!")
}

suspend fun foo() {
    println("Foo executed!")
}