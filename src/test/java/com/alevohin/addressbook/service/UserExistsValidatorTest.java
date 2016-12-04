package com.alevohin.addressbook.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

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

    @Test
    public void errorIfUserExists() {

    }

    public void okIfNoUserExists() {

    }
}
