package scc.food.reward;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author Celine
 * @since 2021/08/04
 */
@MapperScan(value = {"scc.food.reward.mapper", "scc.food.mq.mapper"})
@SpringBootApplication
@ComponentScan("scc.food")
public class ServiceRewardApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceRewardApplication.class, args);
	}

}
