package com.example.BookMyShow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;


@SpringBootApplication
@EnableScheduling
public class BookMyShowApplication {

	public static HashMap<String, Integer> userNameMap = new HashMap<>();
	public static void main(String[] args) {

		SpringApplication.run(BookMyShowApplication.class, args);
	}
}


