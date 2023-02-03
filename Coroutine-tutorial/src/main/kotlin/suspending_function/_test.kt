package suspending_function

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {
    (1..2).forEach { num ->
        launch {          withContext(Dispatchers.Default){

            longRunningTask(num, num + 1)
            anotherJob()
        }}



    }
}

suspend fun longRunningTask(input1: Int, input2: Int): Int {
    log("Start calculation. : input1 : $input1, input2 : $input2")
    delay(2000)
    val intermediateResult = input1 + input2
    log("Intermediate result has been calculated (input1 + input2). : $intermediateResult")
    delay(2000)
    val finalResult = intermediateResult * 2
    log("All of the calculation process have done (result * 2). : $finalResult")
    return finalResult
}

suspend fun anotherJob(){
    log("another Job Start!")
    delay(2000)
}
private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}