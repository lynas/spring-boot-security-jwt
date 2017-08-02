package com.lynas.springbootsecurity.dao

import com.lynas.springbootsecurity.model.AuthUser
import org.springframework.data.repository.CrudRepository


interface AuthUserDao : CrudRepository<AuthUser, Long> {

    fun getAuthUserByUsername(name: String): AuthUser
}