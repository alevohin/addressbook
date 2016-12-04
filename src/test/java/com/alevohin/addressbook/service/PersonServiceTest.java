package com.alevohin.addressbook.service;

import com.alevohin.addressbook.domain.Person;
import com.alevohin.addressbook.domain.PersonEvent;
import com.alevohin.addressbook.domain.PersonRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.vaadin.spring.events.EventBus;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class PersonServiceTest {

    private PersonService service;

    @Mock
    private EventBus.UIEventBus eventBus;

    @Mock
    private PersonRepository personRepository;

    @Captor
    private ArgumentCaptor<Object> eventObject;

    @Before
    public void before() {
        initMocks(this);
        service = new PersonService(eventBus, personRepository);
    }

    @Test
    public void saveNewPerson() {
        Person person = mock(Person.class);
        when(person.getId()).thenReturn(0L);
        service.save(person);

        verify(personRepository).save(person);
        verify(eventBus).publish(eq(service), eventObject.capture());
        final Object event = eventObject.getValue();
        assertTrue(event instanceof PersonEvent.Created);
        assertEquals(((PersonEvent.Created) event).getEntity(), person);
    }

    @Test
    public void saveExistedPerson() {
        Person person = mock(Person.class);
        when(person.getId()).thenReturn(10L);
        service.save(person);

        verify(personRepository).save(person);
        verify(eventBus).publish(eq(service), eventObject.capture());
        final Object event = eventObject.getValue();
        assertTrue(event instanceof PersonEvent.Updated);
        assertEquals(((PersonEvent.Updated) event).getEntity(), person);
    }

    @Test
    public void deletePerson() {
        Person person = mock(Person.class);
        service.delete(person);

        verify(personRepository).delete(person);
        verify(eventBus).publish(eq(service), eventObject.capture());
        final Object event = eventObject.getValue();
        assertTrue(event instanceof PersonEvent.Deleted);
        assertEquals(((PersonEvent.Deleted) event).getEntity(), person);
    }

    @Test
    public void createPerson() {
        final String username = randomAlphanumeric(10);
        final Person person = service.newPerson(username);
        assertEquals(person.getUsername(), username);
    }
}
