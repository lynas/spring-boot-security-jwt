package com.lynas.springbootsecurity.model.request_response

class AuthenticationRequest(val username: String, val password: String)
class AuthenticationResponse(val token: String)