package com.example.springkotlingradle.repository

import com.example.springkotlingradle.entity.Owner
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID


interface OwnerRepository :JpaRepository<Owner,UUID>{
}