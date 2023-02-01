package com.example.demo

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DemoApplication

fun main(args: Array<String>) {
	val jsonstr = util.getJson()
	val obj = JSON.parse(jsonstr) as Map<*,*>
	val dataArray = obj.get("results") as JSONArray
	println(dataArray.size)
	dataArray.forEach{
		val list = (it as Map<*,*>).get("urls") as Map<*,*>
		list.forEach{
			println(it.key)
			println(it.value)
		}
	}





	// runApplication<DemoApplication>(*args)





}
