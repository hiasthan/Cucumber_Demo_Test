package com.example.CucumberBDDTest;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CucumberBddTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(CucumberBddTestApplication.class, args);
	}

}
