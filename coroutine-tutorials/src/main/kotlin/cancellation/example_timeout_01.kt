package cancellation

import kotlinx.coroutines.*

fun main() = runBlocking {

    withTimeout(1300){
        repeat(1000){i->
            println("I'm sleeping...")
            delay(500)
        }
    }

}
