package com.alevohin.addressbook.view;

import com.alevohin.addressbook.domain.User;
import com.alevohin.addressbook.service.UserService;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.*;
import org.vaadin.viritin.label.RichText;

public class LoginView extends VerticalLayout {

    private final TextField username = new TextField("Username");
    private final PasswordField password = new PasswordField("Password");
    private final Button login = new Button("Login", evt -> login());
    private final Button register = new Button("Register", evt -> showRegister());
    private final RichText about = new RichText().withMarkDownResource("/readme.md");

    private final RegisterForm registerForm;
    private final UserService userService;
    private final UserService.LoginCallback loginCallback;

    public LoginView(RegisterForm registerForm, UserService userService, UserService.LoginCallback loginCallback) {
        this.registerForm = registerForm;
        this.userService = userService;
        this.loginCallback = loginCallback;

        setMargin(true);
        setSpacing(true);


        addComponent(username);
        addComponent(password);
        final HorizontalLayout buttons = new HorizontalLayout(login, register);
        buttons.setSpacing(true);
        addComponent(buttons);
        addComponent(about);

        login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
    }

    private void login() {
        if (!userService.login(username.getValue(), password.getValue())) {
            Notification.show("Login failed");
            username.focus();
        } else {
            loginCallback.onLogin();
        }
    }

    private void showRegister() {
        User user = userService.createUser();
        registerForm.setModalWindowTitle("Register new user");
        registerForm.setEntity(user);
        registerForm.openInModalPopup();
    }
}
