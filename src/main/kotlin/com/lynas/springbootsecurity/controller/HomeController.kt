package com.lynas.springbootsecurity.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping


@Controller
class HomeController{

    @RequestMapping("/")
    fun home() : String{
        return "home"
    }
}