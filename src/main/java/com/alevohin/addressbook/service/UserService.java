package com.alevohin.addressbook.service;

import com.alevohin.addressbook.domain.User;
import com.alevohin.addressbook.domain.UserRepository;
import com.vaadin.server.VaadinService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringComponent
@UIScope
public class UserService {

    private final AuthenticationManager authenticationManager;
    private final PersonService personService;
    private final UserRepository userRepository;
    private final PasswordToHash passwordToHash;

    public UserService(@Autowired AuthenticationManager authenticationManager,
                       @Autowired UserRepository userRepository,
                       @Autowired PersonService personService,
                       @Autowired PasswordToHash passwordToHash) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.personService = personService;
        this.passwordToHash = passwordToHash;
    }

    /**
     * Login in application.
     * @param username
     * @param password
     * @return true is logged in.
     */
    public boolean login(String username, String password) {
        try {
            final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, password);
            Authentication token = authenticationManager.authenticate(auth);
            VaadinService.reinitializeSession(VaadinService.getCurrentRequest());
            SecurityContextHolder.getContext().setAuthentication(token);
            return true;
        } catch (AuthenticationException ex) {
            return false;
        }
    }

    /**
     * Create new user.
     */
    public User createUser() {
        return new User();
    }

    /**
     * Save newly created User.
     */
    public void saveNew(User user) {
        try {
            String passwordHash = passwordToHash.convert(user.getPassword());
            user.setPassword(passwordHash);
            userRepository.save(user);
            personService.addMeToContacts(user.getUsername());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Find User by login.
     * @param login - username. Case will be ignored.
     * @return found User
     */
    public User find(String login) {
        return userRepository.findByUsernameIgnoreCase(login);
    }

    /**
     * Callback for login event.
     */
    public interface LoginCallback {
        void onLogin();
    }

    /**
     * Callback for logout event.
     */
    public interface LogoutCallback {
        void onLogout();
    }
}
