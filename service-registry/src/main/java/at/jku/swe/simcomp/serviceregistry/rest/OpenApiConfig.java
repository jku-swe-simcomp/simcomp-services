package at.jku.swe.simcomp.serviceregistry.rest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Redirects the root path to the swagger-ui.html
 */
@Configuration
public class OpenApiConfig implements WebMvcConfigurer {
    /**
     * Redirects the root path to the swagger-ui.html
     * @param registry the registry
     */
    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/swagger-ui.html");
    }
}

