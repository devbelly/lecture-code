package com.devbelly;

import com.devbelly.entity.Product;
import com.devbelly.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class SpringBootKafkaApplication {

	@Autowired
	private ProductRepository productRepository;

	@PostConstruct
	public void ps(){
		Product product = Product.of("맥북",1000L);
		productRepository.save(product);
	}


	public static void main(String[] args) {
		SpringApplication.run(SpringBootKafkaApplication.class, args);
	}

	/**
	 * kafkaTemplate를 인자로 받아 Topic을 전송한다
	 */
//	@Bean
//	CommandLineRunner commandLineRunner(KafkaTemplate<String,String> kafkaTemplate){
//		return args ->{
//			kafkaTemplate.send("amigoscode","hello kafka");
//		};
//	}
}
