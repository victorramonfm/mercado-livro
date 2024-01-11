package com.mercadolivro.config

import com.mercadolivro.enum.Role
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.AuthorizationFilter
import com.mercadolivro.security.AuthenticationFilter
import com.mercadolivro.security.CustomAuthenticationEntryPoint
import com.mercadolivro.security.JwtUtil
import com.mercadolivro.service.UserDetailsCustomService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter

@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val customerRepository: CustomerRepository,
    private val userDetails: UserDetailsCustomService,
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JwtUtil,
    private val customEntryPoint: CustomAuthenticationEntryPoint,
) {

    private val PUBLIC_MATCHERS = arrayOf<String>()

    private val PUBLIC_POST_MATCHERS = arrayOf(
        "/customer"
    )

    private val ADMIN_MATCHERS = arrayOf(
        "/admin/**"
    )

    fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetails).passwordEncoder(bCryptPasswordEncoder())
    }

//    @Bean
//    fun configure(http: HttpSecurity): SecurityFilterChain {
//        http
//            .authorizeHttpRequests { authz ->
//                authz
//                    .requestMatchers(*PUBLIC_MATCHERS).permitAll()
//                    .requestMatchers(HttpMethod.POST, *PUBLIC_POST_MATCHERS).permitAll()
//                    .requestMatchers(*ADMIN_MATCHERS).hasAuthority(Role.ADMIN.description)
//                    .anyRequest().authenticated()
//            }
//            .addFilter(AuthenticationFilter(authenticationManager(), customerRepository, jwtUtil))
//            .addFilter(AuthorizationFilter(authenticationManager(), userDetails, jwtUtil))
//            .exceptionHandling { auth ->
//                auth
//                    .authenticationEntryPoint(customEntryPoint)
//            }
//
//        return http.build()
//    }

//    @Bean
//    fun configure(web: WebSecurity){
//        web.ignoring().requestMatchers(
//            "/v2/api-docs",
//            "/configuration/ui",
//            "/swagger-resources/**",
//            "/configuration/**",
//            "/swagger-ui.html",
//            "/webjars/**"
//        )
//    }

//    @Bean
//    fun corsConfig(): CorsFilter {
//        val source = UrlBasedCorsConfigurationSource()
//        val config = CorsConfiguration()
//        config.allowCredentials = true
//        config.addAllowedOrigin("*")
//        config.addAllowedHeader("*")
//        config.addAllowedMethod("*")
//        source.registerCorsConfiguration("/**", config)
//        return CorsFilter(source)
//    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(): AuthenticationManager {
        return authenticationConfiguration.authenticationManager
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}