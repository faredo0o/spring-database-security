package com.faredo0o.springdatabasesecurity.registration;

import com.faredo0o.springdatabasesecurity.appuser.AppUser;
import com.faredo0o.springdatabasesecurity.appuser.AppUserRole;
import com.faredo0o.springdatabasesecurity.appuser.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor

public class RegistrationService {
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    public String register(RegistrationRequest request) {
        boolean isValidEmail= emailValidator.test(request.getEmail());
        if(!isValidEmail){
            throw new IllegalArgumentException("Email not valid");
        }
        return appUserService.signUpUser(
                new AppUser(request.getFirstName()
                        , request.getLastName()
                        ,request.getPassword()
                        , AppUserRole.USER
                ,false
                ,false
                ,request.getEmail())
        );
    }
}
