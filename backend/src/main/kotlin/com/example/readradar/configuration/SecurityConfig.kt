package com.example.readradar.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeRequests { authz ->
                authz.requestMatchers("/authenticate").permitAll()
                authz.anyRequest().authenticated()
            }
            .csrf().disable()
            .httpBasic { }
            .authenticationManager(CustomAuthenticationManager())
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

//    @Bean
//    fun configureGlobal(auth: AuthenticationManagerBuilder): DaoAuthenticationConfigurer<AuthenticationManagerBuilder, MyUserDetailsService>? {
//        return auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder())
//    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
