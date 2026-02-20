package org.quintilis.forum.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests { auth ->
                // Leitura pública
                auth.requestMatchers(HttpMethod.GET, "/**").permitAll()
                
                // Escrita só para ADMIN
                // O Spring Security adiciona o prefixo "ROLE_" automaticamente se usarmos hasRole
                // Se o token vier com "roles": ["ADMIN"], o converter abaixo deve tratar isso
                auth.requestMatchers(HttpMethod.POST, "/**").hasAuthority("ROLE_ADMIN")
                auth.requestMatchers(HttpMethod.PUT, "/**").hasAuthority("ROLE_ADMIN")
                auth.requestMatchers(HttpMethod.DELETE, "/**").hasAuthority("ROLE_ADMIN")
                
                auth.anyRequest().authenticated()
            }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtAuthenticationConverter())
                }
            }

        return http.build()
    }

    // Converte as claims do JWT para Authorities do Spring
    private fun jwtAuthenticationConverter(): JwtAuthenticationConverter {
        val grantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        grantedAuthoritiesConverter.setAuthoritiesClaimName("roles") // O nome do campo no JWT
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_") // Prefixo padrão

        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
}
