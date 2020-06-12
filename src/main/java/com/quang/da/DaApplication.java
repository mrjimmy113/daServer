package com.quang.da;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DaApplication.class, args);
	}

}
