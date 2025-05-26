package com.ftcompany.user_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AccountService accountService;
    private Environment environment;

    public AuthenticationFilter(AuthenticationManager authenticationManager,
                                AccountService accountService,
                                Environment environment) {
        super(authenticationManager);
        this.accountService = accountService;
        this.environment = environment;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginForm loginForm = new ObjectMapper().readValue(request.getInputStream(),LoginForm.class);
            return getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(loginForm.getEmail(), loginForm.getPassword(), new ArrayList<>()));
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    /*
    *
    * 로그인이 성공하고 jwt 토큰 생성후 전달
    * */
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        String email = authResult.getName();
        Account account = accountService.findByEmail(email);

        String token = Jwts.builder()
                .setSubject(account.getAccountId())
                .setExpiration(new Date(System.currentTimeMillis()+ Long.parseLong(environment.getProperty("token.expiration_time"))))
                .signWith(SignatureAlgorithm.HS512, environment.getProperty("token.secret"))
                .compact();
        response.addHeader("token", token);
        response.addHeader("userId", account.getAccountId());
    }
}
