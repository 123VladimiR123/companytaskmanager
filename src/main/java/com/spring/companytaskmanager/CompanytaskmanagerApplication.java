package com.spring.companytaskmanager;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
public class CompanytaskmanagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CompanytaskmanagerApplication.class, args);
	}

	@Bean
	public Logger getLogger() {
		return LoggerFactory.getLogger(CompanytaskmanagerApplication.class);
	}

}
