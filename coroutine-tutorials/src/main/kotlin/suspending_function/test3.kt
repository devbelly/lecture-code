package suspending_function

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main(args: Array<String>) = runBlocking {
    val time = measureTimeMillis {
        runBlocking {
            for(i: Int in 1..10){
                CoroutineScope(Dispatchers.Default).launch { delay(100)
                    log("hi") }

            }
        }



        println("hi")
    }
    log("Completed in $time ms")
}

private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}