package at.jku.swe.simcomp.azureadapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * The {@code AzureAdaptorApplication} class represents the entry point for the Azure application,
 * showcasing the integration of an adaptor with the service registry.
 *
 * <p>This class is annotated with {@link SpringBootApplication}, indicating that it is the main
 * class for a Spring Boot application. Additionally, it uses {@link ComponentScan} to specify the
 * base packages that should be scanned for Spring components, controllers, and other annotated beans.</p>
 *
 * <p>The main method ({@link #main(String[])}) is responsible for starting the Spring Boot application.</p>
 *
 * <p>This application demonstrates the integration of an adaptor into the service registry and is
 * configured to scan packages under "at.jku.swe.simcomp".</p>
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"at.jku.swe.simcomp"})
public class AzureAdaptorApplication {

	/**
	 * The main method to start the Spring Boot application.
	 *
	 * @param args Command-line arguments passed to the application.
	 */
	public static void main(String[] args) {
		SpringApplication.run(AzureAdaptorApplication.class, args);
	}

}
