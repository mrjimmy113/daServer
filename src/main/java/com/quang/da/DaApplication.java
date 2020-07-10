package com.quang.da;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DaApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(DaApplication.class, args);
	}

}
