package at.jku.swe.simcomp.manager.config;

import at.jku.swe.simcomp.commons.adaptor.execution.command.visitor.impl.ExecutionCommandValidationVisitor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for the execution command validation.
 */
@Configuration
public class ExecutionCommandValidation {

    /**
     * Bean for the execution command validation visitor.
     * @return the execution command validation visitor
     */
    @Bean
    ExecutionCommandValidationVisitor getExecutionCommandValidator(){
        return new ExecutionCommandValidationVisitor();
    }
}
