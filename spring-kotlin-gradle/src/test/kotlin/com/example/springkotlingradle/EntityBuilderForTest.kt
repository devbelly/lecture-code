package com.example.springkotlingradle

import com.example.springkotlingradle.entity.Owner
import com.example.springkotlingradle.entity.Team

fun team(owner: Owner = owner()) = Team(owner=owner)

fun owner(name:String ="admin") = Owner(name=name)