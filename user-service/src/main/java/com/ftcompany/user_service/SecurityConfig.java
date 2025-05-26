package com.ftcompany.user_service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;

    private final Environment environment;

    private static final String[] WHITE_LIST = {
            "/h2-console/**",
            "/health-check",
            "/actuator/**"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(accountService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        http.csrf(AbstractHttpConfigurer::disable);
        http.authorizeHttpRequests(
                authz -> authz
                        .requestMatchers(WHITE_LIST).permitAll()
                        .requestMatchers(new IpAddressMatcher("175.116.181.23")).permitAll()
                        .requestMatchers(new IpAddressMatcher("127.0.0.1")).permitAll()
                        .requestMatchers(new IpAddressMatcher("172.18.0.5")).permitAll()
                        .requestMatchers(new IpAddressMatcher("172.18.0.0")).permitAll()
                        .requestMatchers(new IpAddressMatcher("::1")).permitAll()
                )
            .authenticationManager(authenticationManager)
            .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        http.addFilter(getAuthenticationFilter(authenticationManager));
        http.headers(headers->headers.frameOptions((frameOptions->frameOptions.sameOrigin())));
        return http.build();

    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager AuthenticationManager) {
        return new AuthenticationFilter(AuthenticationManager, accountService, environment);
    }
}
