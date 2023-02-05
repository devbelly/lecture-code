package basic

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    launch {
        repeat(5){i->
            println("Coroutine A, $i")
        }
    }

    launch {
        repeat(5){i->
            println("Coroutine B, $i")
        }
    }
    println("Coroutine outer")
}

fun <T> println(msg: T){
    kotlin.io.println("$msg [${Thread.currentThread().name}]")
}