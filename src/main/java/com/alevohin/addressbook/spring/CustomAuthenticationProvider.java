package com.alevohin.addressbook.spring;

import com.alevohin.addressbook.domain.UserRepository;
import com.alevohin.addressbook.service.PasswordToHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final PasswordToHash passwordToHash;

    public CustomAuthenticationProvider(@Autowired UserRepository userRepository,
                                        @Autowired PasswordToHash passwordToHash) {
        this.userRepository = userRepository;
        this.passwordToHash = passwordToHash;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String login = authentication.getPrincipal().toString();
        String password = passwordToHash.convert(authentication.getCredentials().toString());
        if (userRepository.findByUsernameAndPassword(login, password) != null) {
            return new UsernamePasswordAuthenticationToken(
                    authentication.getName(),
                    authentication.getCredentials(),
                    Collections.emptyList());
        } else {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}