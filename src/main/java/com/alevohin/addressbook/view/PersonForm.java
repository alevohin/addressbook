package com.alevohin.addressbook.view;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import com.alevohin.addressbook.domain.Person;
import com.alevohin.addressbook.service.PersonService;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScope
@SpringComponent
public class PersonForm extends AbstractForm<Person> {

    private TextField name = new MTextField("Name");
    private TextField phoneNumber = new MTextField("Phone");
    private TextField email = new MTextField("Email");

    PersonForm(PersonService service) {
        setSizeUndefined();

        setSavedHandler(person -> {
            service.save(person);
            closePopup();
        });
        setDeleteHandler(person -> {
            service.delete(person);
            closePopup();
        });
        setResetHandler(person -> closePopup());
    }

    @Override
    protected Component createContent() {
        email.setNullRepresentation("");
        phoneNumber.setNullRepresentation("");
        return new MVerticalLayout(
                new MFormLayout(name, email, phoneNumber).withWidth(""),
                getToolbar()
        ).withWidth("");
    }
}
