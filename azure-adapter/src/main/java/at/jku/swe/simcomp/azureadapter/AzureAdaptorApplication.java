package at.jku.swe.simcomp.azureadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Azure application showing integration of an adaptor with the service-registry.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"at.jku.swe.simcomp"})
public class AzureAdaptorApplication {

	public static void main(String[] args) {
		SpringApplication.run(AzureAdaptorApplication.class, args);
	}

}
