package com.faredo0o.springdatabasesecurity.registration;

import com.faredo0o.springdatabasesecurity.appuser.AppUser;
import com.faredo0o.springdatabasesecurity.appuser.AppUserRole;
import com.faredo0o.springdatabasesecurity.appuser.AppUserService;
import com.faredo0o.springdatabasesecurity.registration.token.ConfirmationToken;
import com.faredo0o.springdatabasesecurity.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.Period;

@Service
@AllArgsConstructor
@Transactional
public class RegistrationService {
    private final EmailValidator emailValidator;
    private final AppUserService appUserService;
    private final ConfirmationTokenService confirmationTokenService;
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
    public String confirmToken(String token){
       ConfirmationToken confirmationToken= confirmationTokenService.getToken(token);
       if(confirmationToken.getConfirmedAt()!=null){

           throw new IllegalStateException("Email already confirmed ");
       }
        LocalDateTime expiresAt=confirmationToken.getExpiredAt();
     if(expiresAt.isBefore(LocalDateTime.now())){
         throw new IllegalStateException("Token Expired !");
     }
     confirmationTokenService.setConfirmedAt(confirmationToken);
     appUserService.enableAppUser(confirmationToken.getAppUser().getEmail());
     return "Confirmed";


    }
}
