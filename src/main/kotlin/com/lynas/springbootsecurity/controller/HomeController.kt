package com.lynas.springbootsecurity.controller

import com.lynas.springbootsecurity.dao.AuthUserDao
import com.lynas.springbootsecurity.model.AuthUser
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController(val authUserDao: AuthUserDao){

    @RequestMapping("/")
    fun home() : String{
        return "home"
    }

    @RequestMapping("/login")
    fun login() : String{
        return "login"
    }

    @RequestMapping("/createAuthUser")
    fun createAuthUser() : String{
        val authUser = AuthUser().apply {
            username = "sazzad"
            password = "1"
        }
        authUserDao.save(authUser)
        return "home"
    }


    @RequestMapping("/getAuthUserList")
    fun getAuthUserList() : String{
        return "home"
    }




}