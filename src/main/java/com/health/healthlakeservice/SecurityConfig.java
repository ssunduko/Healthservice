package com.health.healthlakeservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@Configuration
/**
 * Security configuration
 *
 *  This was adapted from a post "Introduction to Java Config for Spring Security"
 *  by baelgung
 *  https://www.baeldung.com/java-config-spring-security
 */
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${spring.security.user.name}")
    private String basicauthAccessKey;

    @Value("${spring.security.user.password}")
    private String basicauthSecretKey;

    @Override
    /**
     * Configure basic auth
     */
    protected void configure(HttpSecurity http) throws Exception{
        http.cors().and().csrf().disable();
        http
                .cors()
                .and()
                .authorizeRequests()
                .antMatchers(
                        "/v3/api-docs/**")
                .permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic();

    }

    @Override
    /**
     * Configure AWS Secret Manager
     */
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser(basicauthAccessKey)
                .password(passwordEncoder().encode(basicauthSecretKey))
                .authorities("ADMIN");
    }

    @Bean
    /**
     * Get password encoder
     */
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}