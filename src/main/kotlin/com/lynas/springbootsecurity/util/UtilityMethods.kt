package com.lynas.springbootsecurity.util

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

fun responseOK(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.OK)
}

fun responseConflict(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.CONFLICT)
}

fun responseError(responseObject: Any): ResponseEntity<*> {
    return ResponseEntity(responseObject, HttpStatus.BAD_REQUEST)
}