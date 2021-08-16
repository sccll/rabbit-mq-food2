package scc.food.order;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Celine
 * @since 2021/08/03
 */
@MapperScan(value = {"scc.food.order.mapper", "scc.food.mq.mapper"})
@SpringBootApplication
@ComponentScan("scc.food")
public class ServiceOrderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceOrderApplication.class, args);
	}

}
