package suspending_function

import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main(args: Array<String>) = runBlocking {
    launch(CoroutineName("Coroutine:A")) {

        launch(CoroutineName("Coroutine:B-1")) {
            repeat(5) {
                delay(1000)
                log("Processing : ${it + 1} / 3")
            }

        }

        launch {
            val a= async(CoroutineName("Coroutine:C-1")) {
                repeat(3) {
                    delay(1000)
                    log("Processing : ${it + 1} / 3")
                }
            }

            val b= async(CoroutineName("Coroutine:C-2")) {
                repeat(6){
                    delay(500)
                    log("Processing : ${it + 1} / 5")
                    try{
                        if (it > 0) throw RuntimeException("Something wrong!")
                    }catch (e :Exception){
                        println("내부에서 catch")
                    }

                }

            }

            try {
                a.await()
                b.await()
            }catch (e:Exception){
                println("error occurer")
            }
//            runCatching {
//                a.await()
//                b.await()
//            }.onFailure { throwable->
//                println("error occured")
//            }

        }
    }
    println("hi")
}



private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}