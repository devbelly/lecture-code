package suspending_function

import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

// Dkotlinx.coroutines.debug
fun main()= runBlocking<Unit> {
    val time = measureTimeMillis {
        val one = async {doSomethingOne()}
        val two = async { doSomethingTwo() }
        log("The answer is ${one.await()+two.await()}")
    }
    log("Completed in $time ms")
}

suspend fun doSomethingOne():Int{
    log("one start...")
    delay(1000L)
    log("one end...")
    return 13
}

suspend fun doSomethingTwo():Int{
    log("two start...")
    delay(1000L)
    log("two end...")
    return 13
}


private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}