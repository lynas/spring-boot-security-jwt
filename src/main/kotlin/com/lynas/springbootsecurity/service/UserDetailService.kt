package com.lynas.springbootsecurity.service

import com.lynas.model.util.SpringSecurityUser
import com.lynas.springbootsecurity.dao.AuthUserDao
import com.lynas.springbootsecurity.model.AuthUser
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.ScopedProxyMode
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Created by lynas on 3/8/2017
 */
@Service("UserDetailsService")
@ComponentScan(scopedProxy = ScopedProxyMode.INTERFACES)
class UserDetailService(val authUserDao: AuthUserDao) : UserDetailsService {


    override fun loadUserByUsername(userName: String): UserDetails {
        val authUser: AuthUser? = authUserDao.getAuthUserByUsername(userName)
        if (authUser == null) {
            throw UsernameNotFoundException(String.format("No user found with username '%s'" + userName))
        } else {
            return SpringSecurityUser(
                    authUser.id,
                    authUser.username,
                    authUser.password,
                    null,
                    null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList(authUser.authorities)
            )
        }

    }
}