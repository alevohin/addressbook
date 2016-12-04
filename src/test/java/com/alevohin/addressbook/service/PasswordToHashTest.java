package com.alevohin.addressbook.service;

import org.junit.Before;
import org.junit.Test;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.Assert.assertNotEquals;

public class PasswordToHashTest {

    private PasswordToHash passwordToHash;

    @Before
    public void before() {
        passwordToHash = new PasswordToHash();
    }

    @Test
    public void saltIsUsed() {
        String password = randomAlphanumeric(10);
        passwordToHash.setSalt("");
        final String passwordHash1 = passwordToHash.convert(password);

        passwordToHash.setSalt(randomAlphanumeric(5));
        final String passwordHash2 = passwordToHash.convert(password);

        assertNotEquals(passwordHash1, passwordHash2);
    }
}
