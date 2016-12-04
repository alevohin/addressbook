package com.alevohin.addressbook.view;

import com.alevohin.addressbook.domain.Person;
import com.alevohin.addressbook.domain.PersonEvent;
import com.alevohin.addressbook.service.UserService;
import com.alevohin.addressbook.service.PersonService;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.*;
import com.vaadin.ui.Button.ClickEvent;
import org.springframework.stereotype.Component;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;
import org.vaadin.spring.events.annotation.EventBusListenerMethod;
import org.vaadin.viritin.button.ConfirmButton;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@Component
@UIScope
public class PersonView extends VerticalLayout {

    private final PersonService personService;
    private final PersonForm personForm;
    private final EventBus.UIEventBus eventBus;

    private String username;

    private MTable<Person> personTable = new MTable<>(Person.class)
            .withProperties("name", "email", "phoneNumber")
            .withColumnHeaders("Name", "Email", "Phone Number")
            .setSortableProperties("name", "email", "phoneNumber")
            .withFullWidth();

    private TextField filterTextField = new MTextField()
            .withInputPrompt("Filter by name");
    private Button addButton = new MButton(FontAwesome.PLUS, "Add", this::add);
    private Button editButton = new MButton(FontAwesome.PENCIL_SQUARE_O, "Edit", this::edit);
    private Button deleteButton = new ConfirmButton(
            FontAwesome.TRASH_O,
            "Delete",
            "Are you sure you want to delete the person?",
            this::remove
    );

    public PersonView(PersonService personService, PersonForm personForm, EventBus.UIEventBus eventBus) {
        this.personService = personService;
        this.personForm = personForm;
        this.eventBus = eventBus;
    }

    void init(String username, UserService.LogoutCallback callback) {
        this.username = username;

        Button logout = new MButton(FontAwesome.SIGN_OUT, "Logout " + username, event -> callback.onLogout());

        addComponent(
                new MVerticalLayout(
                        new MHorizontalLayout(filterTextField, addButton, editButton, deleteButton, logout),
                        personTable
                ).expand(personTable)
        );

        loadPersonTable();

        personTable.addMValueChangeListener(e -> updateButtonStates());
        filterTextField.addTextChangeListener(e -> loadPersonTable(e.getText()));
        eventBus.subscribe(this);
    }

    private void updateButtonStates() {
        boolean hasSelection = personTable.getValue() != null;
        editButton.setEnabled(hasSelection);
        deleteButton.setEnabled(hasSelection);
    }

    private void loadPersonTable() {
        loadPersonTable(filterTextField.getValue());
    }

    private void loadPersonTable(String nameFilter) {
        personTable.setRows(personService.findByNameLikeIgnoreCase(nameFilter, username));
        updateButtonStates();
    }

    private void add(ClickEvent clickEvent) {
        personForm.setModalWindowTitle("New person");
        edit(personService.newPerson(username));
    }

    private void edit(ClickEvent e) {
        personForm.setModalWindowTitle("Edit person");
        edit(personTable.getValue());
    }

    private void edit(final Person person) {
        personForm.setEntity(person);
        personForm.openInModalPopup();
    }

    private void remove(ClickEvent e) {
        personService.delete(personTable.getValue());
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonCreated(PersonEvent.Created event) {
        loadPersonTable();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonUpdated(PersonEvent.Updated event) {
        loadPersonTable();
    }

    @EventBusListenerMethod(scope = EventScope.UI)
    public void onPersonDeleted(PersonEvent.Deleted event) {
        loadPersonTable();
        if (event.getEntity().equals(personTable.getValue())) {
            personTable.setValue(null);
        }
    }
}
