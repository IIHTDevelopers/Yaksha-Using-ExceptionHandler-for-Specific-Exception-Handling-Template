package com.yaksha.assignment;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.yaksha.assignment")
public class ExceptionHandlingApplication {

	public static void main(String[] args) throws IOException {
		SpringApplication.run(ExceptionHandlingApplication.class, args);
	}
}
