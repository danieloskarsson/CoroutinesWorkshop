package com.kotlinconf.workshop.tasks.cancellation

import kotlinx.coroutines.*
import kotlin.random.Random

fun main() = runBlocking {
    val myJob = launch {
        while (true) {
            try {
                doSomeWorkThatMayFail()
            } catch (e: MyException) {
//                ensureActive() will rethrow CancellationException in cases where e would be of the type Exception
                println("Oops! ${e.message}")
            }
        }
    }
    delay(2000)
    println("Enough!")
    myJob.cancelAndJoin()
}

suspend fun doSomeWorkThatMayFail() {
    delay(300)
    if (Random.nextBoolean()) {
        println("I'm working...")
    } else {
        throw MyException("Didn't work this time!")
    }
}

class MyException(message: String): Exception(message)