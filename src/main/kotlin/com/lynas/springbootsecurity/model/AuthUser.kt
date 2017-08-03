package com.lynas.springbootsecurity.model

import org.jetbrains.annotations.NotNull
import javax.persistence.*

@Entity
class AuthUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @NotNull
    @Column(unique = true)
    var username : String? = null

    @NotNull
    var password : String? = null

    @NotNull
    var authorities: String? = null

}