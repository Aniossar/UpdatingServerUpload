package com.CalculatorMVCUpload.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery(
                        "SELECT username, password, enabled from users where username = ?")
                .authoritiesByUsernameQuery(
                        "select username as principal, authority as role from public.users where username = ?");
    }

    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and().authorizeRequests().anyRequest().authenticated()
                .and().formLogin().permitAll()
                .and().csrf().disable();
    }
}