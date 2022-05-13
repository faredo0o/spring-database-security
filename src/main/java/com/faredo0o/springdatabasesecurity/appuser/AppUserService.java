package com.faredo0o.springdatabasesecurity.appuser;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {
    private final UserRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;



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

        return "it works";
    }

}
