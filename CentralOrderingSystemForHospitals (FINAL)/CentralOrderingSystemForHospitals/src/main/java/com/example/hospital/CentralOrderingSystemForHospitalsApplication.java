package com.example.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Configuration;


@SpringBootApplication
//@ComponentScan(basePackages = "com.example.hospital")
class CentralOrderingSystemForHospitalsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralOrderingSystemForHospitalsApplication.class, args);
	}

}
