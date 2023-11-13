package at.jku.swe.simcomp.manager.service;

import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.ExecutionCommandValidationVisitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ExecutionCommandValidation {

    @Bean
    ExecutionCommandValidationVisitor getExecutionCommandValidator(){
        return new ExecutionCommandValidationVisitor();
    }
}
