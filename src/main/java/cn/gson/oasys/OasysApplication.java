package cn.gson.oasys;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class OasysApplication {

	public static void main(String[] args) {
		SpringApplication.run(OasysApplication.class, args);
	}
}

