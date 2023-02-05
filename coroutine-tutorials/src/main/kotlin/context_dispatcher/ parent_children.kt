import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    val request = launch {
        repeat(3){i ->
            launch {
                delay(200)
                println("$i done")
            }
        }
    }
    println("z")
}