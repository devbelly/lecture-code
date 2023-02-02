package com.example.springkotlingradle.entity

import com.example.springkotlingradle.owner
import com.example.springkotlingradle.repository.OwnerRepository
import com.example.springkotlingradle.repository.TeamRepository
import com.example.springkotlingradle.team
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager

@SpringBootTest
internal class TeamTest{
    @Autowired
    lateinit var ownerRepository: OwnerRepository

    @Autowired
    lateinit var teamRepository: TeamRepository

    @Autowired
    lateinit var entityManager: EntityManager
    @Test
    @Transactional
    fun test(){
        val owner = owner()
        ownerRepository.save(owner)

        entityManager.flush()
        println(entityManager.contains(owner))
        println("#######line 1")

        val team = team(owner)
        println("##### line 3")
        teamRepository.save(team)

        println("#######line 2")

        val teamId = teamRepository.findById(team.id).get().id
        println("result = $teamId")
        Assertions.assertThat(teamId).isEqualTo(team.id)


    }
}