package com.faredo0o.springdatabasesecurity.registration;

import lombok.AllArgsConstructor;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/v1/registration")
@AllArgsConstructor
public class RegistrationController {
    private RegistrationService registrationService;
    @PostMapping
    public String register(@RequestBody RegistrationRequest request){
        return registrationService.register(request);
    }
    @GetMapping("/confirm")
    public String confirmToken(@RequestParam("token") String token){
      return  registrationService.confirmToken(token);
    }
}
