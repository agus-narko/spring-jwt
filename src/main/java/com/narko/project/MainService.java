package com.narko.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.narko.project.model.audit.AuditorAwareImpl;

@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@SpringBootApplication()
@EnableScheduling
public class MainService {

	private static final Logger log = LogManager.getLogger(MainService.class);

	@Bean
	public AuditorAware auditorAware() {
		return new AuditorAwareImpl();
	}

	public static void main(String[] args) {
		SpringApplication.run(MainService.class, args);
	}

}
