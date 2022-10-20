package com.wizeline.learningjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.io.IOException;

@EnableFeignClients
@SpringBootApplication
public class LearningJavaGradleApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(LearningJavaGradleApplication.class, args);
	}

}
