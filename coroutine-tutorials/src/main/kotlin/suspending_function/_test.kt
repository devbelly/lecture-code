package suspending_function

import kotlinx.coroutines.*

fun main(args: Array<String>) = runBlocking {

    launch{
        log("메인 쓰레드에서 코루틴 실행중...")
        withContext(Dispatchers.IO){
            log("같은 코루틴이지만 다른 쓰레드에서 실행...")
        }
        log("메인 쓰레드는 블로킹으로부터 안전..")
    }
    println("")
}

private fun log(message: String) {
    println("[${Thread.currentThread().name}] : $message")
}