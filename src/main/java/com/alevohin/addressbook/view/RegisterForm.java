package com.alevohin.addressbook.view;

import com.alevohin.addressbook.domain.User;
import com.alevohin.addressbook.service.UserExistsValidator;
import com.alevohin.addressbook.service.UserService;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class RegisterForm extends AbstractForm<User> {

    private TextField username = new MTextField("Username");
    private TextField password = new MTextField("Password");

    RegisterForm(UserService service) {
        setSizeUndefined();
        addValidator(new UserExistsValidator(service), username);

        setSavedHandler(user -> {
            service.saveNew(user);
            closePopup();
            Notification.show("User successfully created");
        });
        setResetHandler(person -> closePopup());
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(username, password).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
