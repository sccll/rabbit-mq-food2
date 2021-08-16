package scc.food.delivery;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan(value = {"scc.food.delivery.mapper", "scc.food.mq.mapper"})
@SpringBootApplication
@ComponentScan("scc.food")
public class ServiceDeliveryApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceDeliveryApplication.class, args);
	}

}
