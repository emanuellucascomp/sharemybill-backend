package br.com.embole.sharemybill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class SharemybillApplication {

	public static void main(String[] args) {
		SpringApplication.run(SharemybillApplication.class, args);
	}

}
