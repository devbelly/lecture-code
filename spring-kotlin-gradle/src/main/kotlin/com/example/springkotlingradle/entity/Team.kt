package com.example.springkotlingradle.entity

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne


@Entity
class Team(
    @OneToOne
    @JoinColumn(name="team_id")
    val owner: Owner
):PrimaryKeyEntity(){}



