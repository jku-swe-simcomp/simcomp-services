package at.jku.swe.simcomp.manager.rest;

import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeKey;
import at.jku.swe.simcomp.commons.adaptor.attribute.AttributeValue;
import at.jku.swe.simcomp.manager.service.AttributeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/session/{sessionId}/attribute/{attributeKey}")
@Slf4j
public class AttributeController {

   private final AttributeService attributeService;
   public AttributeController(AttributeService attributeService){
       this.attributeService = attributeService;
    }

    @GetMapping
    public ResponseEntity<Map<String, AttributeValue>> getAttributes(@PathVariable UUID sessionId, @PathVariable AttributeKey attributeKey){
        log.info("Request to fetch attribute {} for session {}", attributeKey, sessionId);
        return ResponseEntity.ok(attributeService.getAttributeValues(sessionId, attributeKey));
    }
}
