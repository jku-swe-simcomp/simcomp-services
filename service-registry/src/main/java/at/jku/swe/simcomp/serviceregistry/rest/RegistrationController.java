package at.jku.swe.simcomp.serviceregistry.rest;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import at.jku.swe.simcomp.serviceregistry.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Endpoint for registration resources.
 */
@RestController
@RequestMapping("/api/registration")
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    /**
     * Registers an adaptor according to the passed config.
     * @param configDTO the config.
     * @return a response entity indicating the success of the operation
     * @throws AdaptorAlreadyRegisteredException if an adaptor with the same {@link ServiceRegistrationConfigDTO#name}
     * is already registered.
     */
    @PostMapping
    public ResponseEntity<Void> registerService(@RequestBody ServiceRegistrationConfigDTO configDTO) throws AdaptorAlreadyRegisteredException {
        log.info("Registering service: {}", configDTO);
        registrationService.register(configDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * Unregisters an adaptor by deleting all information about it.
     * @param name the name (id) of the adaptor
     * @return a response entity indicating success of the operation.
     */
    @DeleteMapping("/{name}")
    public ResponseEntity<Void> unregisterService(@PathVariable String name){
        log.info("Unregistering service: {}", name);
        registrationService.unregister(name);
        return ResponseEntity.ok().build();
    }

    /**
     * Returns a list with all available registrations.
     * @return the list
     */
    @GetMapping
    public ResponseEntity<List<ServiceRegistrationConfigDTO>> getAll(){
        log.info("Fetching all adaptor configurations");
        return ResponseEntity.ok(registrationService.getAllRegisteredAdaptors());
    }
}
