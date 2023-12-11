package at.jku.swe.simcomp.serviceregistry.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class RedirectionController {

    @GetMapping
    public String redirectToSwagger(){
        return "redirect:/swagger-ui/index.html";
    }
}
