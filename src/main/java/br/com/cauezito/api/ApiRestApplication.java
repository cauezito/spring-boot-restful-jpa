package br.com.cauezito.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EntityScan(basePackages = "br.com.cauezito.api.model")
@ComponentScan(basePackages = "br.com.cauezito.api*")
@EnableJpaRepositories(basePackages = "br.com.cauezito.api.repository")
@RestController
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableCaching

public class ApiRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
		//System.out.println(new BCryptPasswordEncoder().encode(""));
	}

}
