package com.intuitive.care.utils.infra;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/swagger-resources/**")
                .permitAll()
                .anyRequest().permitAll()
                .and()
                .csrf().disable();

        return http.build();
    }
}
