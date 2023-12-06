package at.jku.swe.simcomp.manager.config;

import at.jku.swe.simcomp.manager.service.client.NoExceptionThrowingResponseErrorHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {
    @Bean
    @Primary
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    @Qualifier("noExceptionThrowingRestTemplate")
    public RestTemplate noExceptionThrowingRestTemplate(RestTemplateBuilder restTemplateBuilder,
                                                        NoExceptionThrowingResponseErrorHandler noExceptionThrowingResponseErrorHandler) {
        return restTemplateBuilder
                .errorHandler(noExceptionThrowingResponseErrorHandler)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }


}
