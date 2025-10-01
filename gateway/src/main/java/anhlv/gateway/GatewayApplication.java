package anhlv.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(
    basePackages = {"anhlv.gateway", "com.company.common"},
    excludeFilters = @ComponentScan.Filter(
        type = org.springframework.context.annotation.FilterType.ASSIGNABLE_TYPE,
        classes = com.company.common.apiresponse.GlobalExceptionHandler.class
    )
)
public class GatewayApplication {
	public static void main(String[] args) {
		// Sử dụng cấu hình log4j2 local
		SpringApplication.run(GatewayApplication.class, args);
	}
}
