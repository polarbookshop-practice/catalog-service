package com.polarbookshop.catalogservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeRequests(authorize -> authorize
                        // Allow users to fetch greeting and books without being authenticated
                        .mvcMatchers(HttpMethod.GET, "/", "/books/**").permitAll()
                        // Any other request requires not only authentication
                        // but also the employee role
                        .anyRequest().hasRole("employee")
                )
                // Enables OAuth2 Resource Server support using the default configuration based on JWT authentication
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
                // Each request must include an Access Token, so there's no need to keep
                // a user session alive between requests.
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Since the authentication strategy is stateless and doesn't involve a browser-based client
                // we can safely disable CSRF protection
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter(){
        // Defines a converter to map claims to GrantedAuthority objects
        var jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        // Applies the "ROLE_" prefix to each claim
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
        // Extracts the list of roles from the "roles" claim
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
        // Defines a strategy to convert a JWT. We'll only customize how to build grated authorities out of it
        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }
}
