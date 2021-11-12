package com.mycompany.myteam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.mycompany"})
public class SixLetterWordsApi {

	public static void main(String[] args) {
		SpringApplication.run(SixLetterWordsApi.class, args);
	}

}
