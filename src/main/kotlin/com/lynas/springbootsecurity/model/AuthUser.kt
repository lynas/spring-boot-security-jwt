package com.lynas.springbootsecurity.model

import org.jetbrains.annotations.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long = 0

    @NotNull
    private var username = ""

    @NotNull
    private var password = ""

}