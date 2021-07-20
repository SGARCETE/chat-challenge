package com.asapp.backend.challenge;

import com.asapp.backend.challenge.filter.TokenValidatorFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@SpringBootApplication
public class ChatChallengeApplication {

    @Autowired
    private Environment environment;;

    public static void main(String[] args) {
        SpringApplication.run(ChatChallengeApplication.class, args);
    }

    @EnableWebSecurity
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {
        //TODO: Security configs should not be disabled in tests and this conditional should not be here
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            if(Arrays.stream(environment.getActiveProfiles()).anyMatch(
                    env -> (env.equalsIgnoreCase("test")))) {
                http.csrf().disable()
                        .addFilterAfter(new TokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/users").permitAll()
                        .antMatchers(HttpMethod.POST, "/health").permitAll()
                        .antMatchers(HttpMethod.POST, "/login").permitAll()
                        .antMatchers(HttpMethod.POST, "/messages").permitAll()
                        .antMatchers(HttpMethod.GET, "/messages").permitAll()
                        .anyRequest().authenticated();
            } else {
                http.csrf().disable()
                        .addFilterAfter(new TokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                        .authorizeRequests()
                        .antMatchers(HttpMethod.POST, "/users").permitAll()
                        .antMatchers(HttpMethod.POST, "/health").permitAll()
                        .antMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated();
            }
        }
    }

}

