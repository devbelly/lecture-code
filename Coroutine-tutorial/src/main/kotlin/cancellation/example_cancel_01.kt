package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {
    val startTime = System.currentTimeMillis()

    val job = launch (Dispatchers.Default){
        var nextPrintTime = startTime
        var i = 0
        while (i<5){
            if(System.currentTimeMillis()>=nextPrintTime){
                println("job: I'm sleeping..${i++}")
                nextPrintTime+=500
            }
        }
    }

    delay(1300L)
    println("main: I'm tired of waiting!")
    job.cancelAndJoin()
    println("main: now I'm quit")
}