package com.lynas.springbootsecurity.config

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors


@Component
class JwtTokenUtil(val timeProvider: TimeProvider) {

    val CLAIM_KEY_USERNAME = "sub"
    val CLAIM_KEY_AUDIENCE = "audience"
    val CLAIM_KEY_CREATED = "created"
    val CLAIM_KEY_EXPIRED = "exp"
    val CLAIM_KEY_ROLE = "rlu"

    val AUDIENCE_UNKNOWN = "unknown"
    val AUDIENCE_WEB = "web"
    val AUDIENCE_MOBILE = "mobile"
    val AUDIENCE_TABLET = "tablet"

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun getUsernameFromToken(token: String): String? {
        var username: String?
        try {
            val claims = getClaimsFromToken(token)
            username = claims?.subject
        } catch (e: Exception) {
            username = null
        }
        return username
    }

    fun getUserRoleFromToken(token: String): String? {
        var auth: String?
        try {
            val claims = getClaimsFromToken(token)
            auth = claims?.get(CLAIM_KEY_CREATED) as String
        } catch (e: Exception) {
            auth = null
        }
        return auth
    }

    fun getCreatedDateFromToken(token: String): Date? {
        var created: Date?
        try {
            val claims = getClaimsFromToken(token)
            created = Date(claims?.get(CLAIM_KEY_CREATED) as Long)
        } catch (e: Exception) {
            created = null
        }
        return created
    }

    fun getExpirationDateFromToken(token: String): Date? {
        var expiration: Date?
        try {
            val claims = getClaimsFromToken(token)
            expiration = claims?.expiration
        } catch (e: Exception) {
            expiration = null
        }

        return expiration
    }

    fun getAudienceFromToken(token: String): String? {
        var audience: String?
        try {
            val claims = getClaimsFromToken(token)
            audience = claims?.get(CLAIM_KEY_AUDIENCE)?.toString()
        } catch (e: Exception) {
            audience = null
        }
        return audience
    }

    private fun getClaimsFromToken(token: String): Claims? {
        var claims: Claims?
        try {
            claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .body
        } catch (e: Exception) {
            claims = null
        }
        return claims
    }


    private fun isTokenExpired(token: String): Boolean? {
        val expiration = getExpirationDateFromToken(token)
        return expiration?.before(timeProvider.now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created.before(lastPasswordReset)
    }

    private fun generateAudience(device: Device): String {
        var audience = AUDIENCE_UNKNOWN
        if (device.isNormal) {
            audience = AUDIENCE_WEB
        } else if (device.isTablet) {
            audience = AUDIENCE_TABLET
        } else if (device.isMobile) {
            audience = AUDIENCE_MOBILE
        }
        return audience
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        val audience = getAudienceFromToken(token)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
    }

    fun generateToken(userDetails: UserDetails, device: Device): String {
        val claims = HashMap<String, Any>()

        claims.put(CLAIM_KEY_USERNAME, userDetails.username)
        claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device))
        val createdDate = timeProvider.now()
        claims.put(CLAIM_KEY_CREATED, createdDate)
        claims.put(CLAIM_KEY_ROLE, userDetails.authorities.map { it.authority }.joinToString(","))
        return doGenerateToken(claims)
    }

    private fun doGenerateToken(claims: Map<String, Any>): String {

        val createdDate = claims[CLAIM_KEY_CREATED] as Date
        val expirationDate = Date(createdDate.time + (expiration!! * 1000))

        println("doGenerateToken " + createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean {
        val created = getCreatedDateFromToken(token) ?: return false
        val isTokenExpired = isTokenExpired(token) ?: return false
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired || ignoreTokenExpiration(token))
    }

    fun refreshToken(token: String): String? {
        var refreshedToken: String?
        try {
            val claims = getClaimsFromToken(token) ?: return null
            claims.put(CLAIM_KEY_CREATED, timeProvider.now())
            refreshedToken = doGenerateToken(claims)
        } catch (e: Exception) {
            refreshedToken = null
        }

        return refreshedToken
    }

    fun validateToken(token: String): Boolean {
        getUsernameFromToken(token) ?: return false
        getCreatedDateFromToken(token) ?: return false
        //TODO need to add if token blacklisted
        return isTokenExpired(token) ?: return false
    }

}