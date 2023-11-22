package at.jku.swe.simcomp.manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class AdaptorManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdaptorManagerApplication.class, args);
	}

}
