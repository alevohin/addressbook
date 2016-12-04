package com.alevohin.addressbook.service;

import com.alevohin.addressbook.domain.User;
import com.vaadin.data.Validator;
import org.vaadin.viritin.MBeanFieldGroup;

public class UserExistsValidator implements MBeanFieldGroup.MValidator<User> {

    private static final long serialVersionUID = 7773475835262158065L;

    private final UserService userService;

    public UserExistsValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void validate(User user) throws Validator.InvalidValueException {
        if (user.getUsername() != null && userService.find(user.getUsername()) != null) {
            throw new Validator.InvalidValueException("Username is unavailable");
        }
    }
}