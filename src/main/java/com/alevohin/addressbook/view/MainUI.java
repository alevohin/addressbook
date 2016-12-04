package com.alevohin.addressbook.view;

import com.alevohin.addressbook.service.UserService;
import com.alevohin.addressbook.spring.SecurityUtils;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.beans.factory.annotation.Autowired;

@SpringUI
@Theme(ValoTheme.THEME_NAME)
@Push(transport = Transport.WEBSOCKET_XHR)
public class MainUI extends UI {

    private final UserService authorizationService;
    private final PersonView mainView;

    public MainUI(@Autowired UserService authorizationService, @Autowired PersonView mainView) {
        this.authorizationService = authorizationService;
        this.mainView = mainView;
    }

    @Override
    protected void init(VaadinRequest request) {
        getPage().setTitle("Address Book");
        if (SecurityUtils.isLoggedIn()) {
            showMain();
        } else {
            showLogin();
        }
    }

    private void showLogin() {
        setContent(
                new LoginView(
                        new RegisterForm(authorizationService),
                        authorizationService,
                        this::showMain
                )
        );
    }

    private void showMain() {
        mainView.init(SecurityUtils.getUsername(), this::logout);
        setContent(mainView);
    }

    private void logout() {
        getSession().close();
        getPage().reload();
    }
}
