package scc.food.restaurant;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan(value = {"scc.food.restaurant.mapper", "scc.food.mq.mapper"})
@SpringBootApplication
@ComponentScan("scc.food")
public class ServiceRestaurantApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRestaurantApplication.class, args);
	}

}
