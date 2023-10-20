package at.jku.swe.simcomp.serviceregistry.rest;

import at.jku.swe.simcomp.commons.registry.dto.ServiceRegistrationConfigDTO;
import at.jku.swe.simcomp.serviceregistry.rest.exceptions.AdaptorAlreadyRegisteredException;
import at.jku.swe.simcomp.serviceregistry.service.RegistrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registration")
@Slf4j
public class RegistrationController {
    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService){
        this.registrationService = registrationService;
    }

    @PostMapping
    public ResponseEntity<Void> registerService(@RequestBody ServiceRegistrationConfigDTO configDTO) throws AdaptorAlreadyRegisteredException {
        log.info("Registering service: {}", configDTO);
        registrationService.register(configDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    @RequestMapping("/{name}")
    public ResponseEntity<Void> unregisterService(@PathVariable String name){
        log.info("Unregistering service: {}", name);
        registrationService.unregister(name);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ServiceRegistrationConfigDTO>> getAll(){
        log.info("Fetching all adaptor configurations");
        return ResponseEntity.ok(registrationService.getAllRegisteredAdaptors());
    }
}
