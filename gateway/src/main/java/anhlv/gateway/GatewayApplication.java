package anhlv.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
	public static void main(String[] args) {
		// Sử dụng cấu hình log4j2 local
		SpringApplication.run(GatewayApplication.class, args);
	}
}
