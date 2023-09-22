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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class SecurityConfig(
    private val jwtRequestFilter: JwtRequestFilter
) : SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>() {

    private val unSecuredPaths = arrayOf(
        "/api/auth/**",
        "/api/categories/**",
        "/api/books/**",
        "/h2-console/**"
    )
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        val antPathRequestMatchers: Array<AntPathRequestMatcher> = getAntPathRequestMatchers()
        http
            .authorizeRequests { authz ->
                authz.requestMatchers(antPathRequestMatchers[0]).permitAll()
                authz.requestMatchers(antPathRequestMatchers[1]).permitAll()
                authz.requestMatchers(antPathRequestMatchers[2]).permitAll()
                authz.requestMatchers(antPathRequestMatchers[3]).permitAll()
            }
            .csrf().disable()
            .cors().and()
            .headers().frameOptions().disable().and()
            .httpBasic { }
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("http://localhost:4200")
        configuration.allowedMethods = listOf("*")
        configuration.allowedHeaders = listOf("*")
        configuration.allowCredentials = true
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/api/**", configuration)
        return source
    }

    private fun getAntPathRequestMatchers(): Array<AntPathRequestMatcher> {
        val requestMatchers = arrayOfNulls<AntPathRequestMatcher>(unSecuredPaths.size)
        for (i in unSecuredPaths.indices) {
            requestMatchers[i] = AntPathRequestMatcher(unSecuredPaths[i])
        }
        return requestMatchers.requireNoNulls()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

}
