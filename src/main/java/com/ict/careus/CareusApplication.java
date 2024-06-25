package com.ict.careus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CareusApplication{

	public static void main(String[] args) {
		SpringApplication.run(CareusApplication.class, args);
	}

}
