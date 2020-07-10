package com.muttsapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

@SpringBootApplication
//@EnableJpaRepositories("muttsapp.repositories")
@ComponentScan("com.muttsapp")
public class MuttsApp {

	public static void main(String[] args) {

		SpringApplication.run(MuttsApp.class, args);
	}

}
