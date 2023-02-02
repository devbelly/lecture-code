package com.example.springkotlingradle.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
class Owner(
    @Column(length=255,nullable=false)
    val name:String
):PrimaryKeyEntity(){}
