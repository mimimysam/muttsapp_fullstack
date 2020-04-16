package com.muttsapp.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
 
@Configuration
@EnableAutoConfiguration
//@EnableJpaRepositories("com.muttsapp.repositories")
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
 
	@Autowired
	DataSource dataSource;
 
	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
				.usersByUsernameQuery("select username,password,enabled from user where username=?")
				.authoritiesByUsernameQuery("select u.username, r.role from user u " +
						"join role r " +
						"on u.role = r.id " +
						"where username=?");
	}
 
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/", "/home","/users").permitAll()
//				.antMatchers("/users").hasRole("USER")
				.anyRequest().authenticated()
				.and().formLogin().loginPage("/login").permitAll()
				.failureUrl("/login?error=true")
				.and().csrf().disable().formLogin()
				.defaultSuccessUrl("/home")
				.and().logout().permitAll();

		http.exceptionHandling().accessDeniedPage("/403");
	}
}