package br.ufu.standdize;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StanddizeApplication {

	public static void main(String[] args) {
		SpringApplication.run(StanddizeApplication.class, args);
	}

}
