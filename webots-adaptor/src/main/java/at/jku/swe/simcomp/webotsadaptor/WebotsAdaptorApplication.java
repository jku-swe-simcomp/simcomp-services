package at.jku.swe.simcomp.webotsadaptor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Demo application showing integration of an adaptor with the service-registry.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"at.jku.swe.simcomp"})
public class WebotsAdaptorApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebotsAdaptorApplication.class, args);
    }

}