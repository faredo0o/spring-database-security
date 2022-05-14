package com.faredo0o.springdatabasesecurity.appuser;

import com.faredo0o.springdatabasesecurity.registration.token.ConfirmationToken;
import com.faredo0o.springdatabasesecurity.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;



    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return repository.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException(String.format("User %s not found",email)));
    }
    public String signUpUser(AppUser appUser){
        final boolean userExists = repository.findByEmail(appUser.getEmail()).isPresent();
        if(userExists){
            throw new IllegalArgumentException("Email already taken");

        }
        final String encodedPassword = bCryptPasswordEncoder.encode(appUser.getPassword());
        appUser.setPassword(encodedPassword);
        repository.save(appUser);
        String token=UUID.randomUUID().toString();
        ConfirmationToken confirmationToken=new ConfirmationToken(
                token,
                LocalDateTime.now()
                ,LocalDateTime.now().plusMinutes(15),
                null
                ,appUser);
        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }
    public void enableAppUser(String email){
        Optional<AppUser> appUser=repository.findByEmail(email);
        if(appUser.isPresent()){
            AppUser user=appUser.get();
            user.setEnabled(true);
            repository.save(user);
        }else{
            throw new IllegalStateException("Error enabling user");
        }

    }

}
