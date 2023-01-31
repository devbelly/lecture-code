package com.example.springkotlingradle

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringKotlinGradleApplication

fun main(args: Array<String>) {
	runApplication<SpringKotlinGradleApplication>(*args)
}
