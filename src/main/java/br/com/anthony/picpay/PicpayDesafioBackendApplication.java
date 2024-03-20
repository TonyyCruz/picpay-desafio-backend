package br.com.anthony.picpay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class PicpayDesafioBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PicpayDesafioBackendApplication.class, args);
	}

}
