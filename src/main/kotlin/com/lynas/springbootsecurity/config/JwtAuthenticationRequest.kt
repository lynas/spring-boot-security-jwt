package com.lynas.springbootsecurity.config

class JwtAuthenticationRequest  {

    var username: String? = null
    var password: String? = null

    constructor() : super() {}

    constructor(username: String, password: String) {
        this.username = username
        this.password = password
    }
}
