package com.yohwan.waiting;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableConfigurationProperties({JwtProperties.class, SolapiProperties.class})
@ConfigurationPropertiesScan("com.yohwan.waiting")
@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class WaitingApplication {
	public static void main(String[] args) {
		SpringApplication.run(WaitingApplication.class, args);
	}

}
