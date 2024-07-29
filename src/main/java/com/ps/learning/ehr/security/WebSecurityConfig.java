package com.ps.learning.ehr.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;


@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class WebSecurityConfig {
  public static final String ADMIN = "ADMIN";
  public static final String USER = "USER";

  @Bean
  public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
    return http
        .csrf(ServerHttpSecurity.CsrfSpec::disable)
        .authorizeExchange(auth -> {
          auth.pathMatchers("/swagger-ui/**").permitAll();
          auth.pathMatchers("/swagger-resources/**").permitAll();
          auth.pathMatchers("/v3/api-docs/**").permitAll();
          auth.pathMatchers("/webjars/**").permitAll();
          auth.pathMatchers(HttpMethod.GET).hasAnyRole(USER, ADMIN);
          auth.pathMatchers(HttpMethod.POST).hasAnyRole(USER, ADMIN);
          auth.pathMatchers(HttpMethod.PUT).hasAnyRole(ADMIN);
          auth.anyExchange().authenticated();
        })
        .httpBasic(Customizer.withDefaults())
        .build();
  }

  @Bean
  public MapReactiveUserDetailsService userDetailsService() {
    PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
    UserDetails user = User.withUsername("user")
        .password(encoder.encode("password"))
        .roles(USER)
        .build();
    UserDetails admin = User.withUsername("admin")
        .password(encoder.encode("password"))
        .roles(USER, ADMIN)
        .build();
    return new MapReactiveUserDetailsService(user, admin);
  }
}
