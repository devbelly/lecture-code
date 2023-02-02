package com.example.springkotlingradle.repository

import com.example.springkotlingradle.entity.Team
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeamRepository : JpaRepository<Team,UUID> {
}