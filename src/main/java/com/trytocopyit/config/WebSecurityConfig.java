package com.trytocopyit.config;

import com.trytocopyit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserService userService;

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        //httpSecurity.csrf().disable();
        httpSecurity.authorizeRequests().antMatchers("/admin/orderList", "/admin/order", "/admin/accountInfo")
                .access("hasAnyRole('ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODER')");

        httpSecurity.authorizeRequests().antMatchers(HttpMethod.POST, "/admin/login").permitAll();
        httpSecurity.authorizeRequests().antMatchers("/admin/game").access("hasAnyRole('ROLE_ADMIN', 'ROLE_MODER')");
        httpSecurity.authorizeRequests().and().exceptionHandling().accessDeniedPage("/403");
    }
}
