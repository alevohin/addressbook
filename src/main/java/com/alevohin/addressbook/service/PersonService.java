package com.alevohin.addressbook.service;

import com.alevohin.addressbook.domain.Person;
import com.alevohin.addressbook.domain.PersonEvent;
import com.alevohin.addressbook.domain.PersonRepository;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.vaadin.spring.events.EventBus;

import java.util.List;

/**
 * CRUD service to work with Person.
 */
@SpringComponent
@UIScope
@ConfigurationProperties(prefix = "addressbook.addme")
public class PersonService {

    private EventBus.UIEventBus eventBus;
    private final PersonRepository personRepository;

    private String personName;
    private String personPhone;
    private String personEmail;

    public PersonService(EventBus.UIEventBus eventBus, PersonRepository personRepository) {
        this.personRepository = personRepository;
        this.eventBus = eventBus;
    }

    public Person newPerson(String username) {
        return new Person(username);
    }

    public void save(Person person) {
        boolean newEntity = person.getId() == 0;
        personRepository.save(person);
        eventBus.publish(
                this,
                newEntity ? new PersonEvent.Created(person) : new PersonEvent.Updated(person));
    }

    public void delete(Person person) {
        personRepository.delete(person);
        eventBus.publish(this, new PersonEvent.Deleted(person));
    }

    public List<Person> findByNameLikeIgnoreCase(String nameFilter, String username) {
        return personRepository.findByNameLikeIgnoreCaseAndUsernameOrderById("%" + nameFilter + "%", username);
    }

    /**
     * Add me to contact of newly registered user.
     *
     * @param username - registered user login
     */
    public void addMeToContacts(String username) {
        if (!StringUtils.isEmpty(personName)) {
            Person person = newPerson(username);
            person.setName(personName);
            person.setPhoneNumber(personPhone);
            person.setEmail(personEmail);
            personRepository.save(person);
        }
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public void setPersonPhone(String personPhone) {
        this.personPhone = personPhone;
    }

    public void setPersonEmail(String personEmail) {
        this.personEmail = personEmail;
    }
}
