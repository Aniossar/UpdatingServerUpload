package com.CalculatorMVCUpload;

import com.CalculatorMVCUpload.property.FileStorageProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class MVCMain {

	public static void main(String[] args) {
		SpringApplication.run(MVCMain.class, args);
	}
}
