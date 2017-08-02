package com.lynas.springbootsecurity.model

import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id: Long = 0

    @NotNull
    @Column(unique = true)
    private var username = ""

    @NotNull
    private var password = ""

}