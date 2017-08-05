package com.lynas.springbootsecurity.controller

import com.lynas.springbootsecurity.config.security.JwtTokenUtil
import com.lynas.springbootsecurity.model.request_response.AuthenticationRequest
import com.lynas.springbootsecurity.model.request_response.AuthenticationResponse
import org.springframework.http.ResponseEntity
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController("/auth")
class AuthenticationController(val userDetailsService: UserDetailsService,
                               val authenticationManager: AuthenticationManager,
                               val jwtTokenUtil: JwtTokenUtil) {

    @PostMapping("/login")
    fun login(@RequestBody authenticationRequest: AuthenticationRequest, device: Device): ResponseEntity<*> {
        // Perform the security
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.username,
                        authenticationRequest.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication

        // Reload password post-security so we can generate token
        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails, device)

        // Return the token
        return ResponseEntity.ok<Any>(AuthenticationResponse(token))
    }

}