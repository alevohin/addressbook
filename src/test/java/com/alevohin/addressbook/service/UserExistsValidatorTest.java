package com.alevohin.addressbook.service;

import com.alevohin.addressbook.domain.User;
import com.vaadin.data.Validator;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserExistsValidatorTest {

    private UserExistsValidator validator;

    @Mock
    private UserService userService;

    @Before
    public void before() {
        initMocks(this);
        validator = new UserExistsValidator(userService);
    }

    @Test (expected = Validator.InvalidValueException.class)
    public void errorIfUserExists() {
        when(userService.find(anyString())).thenReturn(mock(User.class));

        User user = new User();
        user.setUsername(randomAlphanumeric(10));
        validator.validate(user);
    }

    @Test
    public void okIfNoUserExists() {
        when(userService.find(anyString())).thenReturn(null);
        User user = new User();
        user.setUsername(randomAlphanumeric(10));
        validator.validate(user);
    }

    @Test
    public void noValidationForEmptyLogin() {
        when(userService.find(anyString())).thenReturn(mock(User.class));

        User user = new User();
        validator.validate(user);
    }
}
