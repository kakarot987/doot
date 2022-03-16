package com.doot;

import com.doot.config.AppProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(AppProperties.class)
public class DootApplication {

	public static void main(String[] args) {
		SpringApplication.run(DootApplication.class, args);
	}

}
