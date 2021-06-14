package com.example.application.views.main;

import com.example.application.models.SystemUser;
import com.example.application.services.SystemUserService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;

@Route
public class LoginView extends VerticalLayout {
    private final SystemUserService systemUserService;

    public LoginView(SystemUserService systemUserService){
        this.systemUserService = systemUserService;
        LoginForm loginForm = new LoginForm();
        loginForm.addLoginListener(loginEvent -> {


            SystemUser result = systemUserService.login(loginEvent.getUsername(),loginEvent.getPassword());

            if (result.getId()!=null)
            {
                VaadinSession.getCurrent().getSession().setAttribute("LoggedInSystemUserId",result.getId());
                UI.getCurrent().getPage().setLocation("/");
            }else
            {
                loginForm.setError(true);
            }
        });
        add(loginForm);
    }
}
