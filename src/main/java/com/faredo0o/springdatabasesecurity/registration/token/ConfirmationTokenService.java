package com.faredo0o.springdatabasesecurity.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepository  confirmationTokenRepository;
    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }
    public ConfirmationToken getToken(String token){
       Optional<ConfirmationToken> confirmationToken=confirmationTokenRepository.findByToken(token);
       if(confirmationToken.isPresent()){
           return confirmationToken.get();
       }
       throw new IllegalStateException("Token not found ");
    }
    public void setConfirmedAt(ConfirmationToken token){
        confirmationTokenRepository.save(token);
    }

}
