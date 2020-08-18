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
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EntityScan(basePackages = "br.com.cauezito.api.model")
@ComponentScan(basePackages = "br.com.cauezito.api*")
@EnableJpaRepositories(basePackages = "br.com.cauezito.api.repository")
@RestController
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableCaching
@EnableWebMvc
public class ApiRestApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ApiRestApplication.class, args);
		System.out.println(new BCryptPasswordEncoder().encode("134679ca"));
	}
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {		
		registry.addMapping("/user/**")
		.allowedMethods("*")
		.allowedOrigins("*")	
		                .allowedHeaders("*")
		                .allowCredentials(true).maxAge(3600);
		    }
		
		
	}


