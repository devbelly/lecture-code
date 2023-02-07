package test

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

fun main() = runBlocking{
    for(i in 1..5){
        try {
            throw RuntimeException("exception")
            break
        } catch (e : Exception) {
            println("$e occered... retry..")
        }
    }

    delay(5000L)
}