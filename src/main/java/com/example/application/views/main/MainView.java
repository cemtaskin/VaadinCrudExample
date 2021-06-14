package com.example.application.views.main;

import com.example.application.models.Person;
import com.example.application.models.SystemUser;
import com.example.application.services.PersonService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Route
public class MainView extends VerticalLayout {

    private final PersonService personService;
    Grid<Person> grid = new Grid<>(Person.class);
    TextField txtFilter = new TextField();
    Dialog dialogPerson=new Dialog();
    Binder<Person> binder = new Binder<>();
    Long itemIdForEdition=0L;
    Long loggedInSystemUserId;


    public MainView(PersonService personService){
        this.personService = personService;

        if (VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId")==null){
            UI.getCurrent().getPage().setLocation("/login");
        }else {

            System.out.println("Logedin User ID");
            System.out.println(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
            loggedInSystemUserId=Long.valueOf(VaadinSession.getCurrent().getSession().getAttribute("LoggedInSystemUserId").toString());
        }



        Button btnNew = new Button("Add", VaadinIcon.INSERT.create());


        txtFilter.setPlaceholder("Key");
        Button btnFilter = new Button("Search", VaadinIcon.SEARCH.create());
        btnFilter.addClickListener(buttonClickEvent -> {
            refreshData(txtFilter.getValue());
        });

        HorizontalLayout filterGroup = new HorizontalLayout();
        filterGroup.add(txtFilter,btnFilter);


        dialogPerson.setModal(true);

        TextField txtName = new TextField("Name","Enter your name");
        TextField txtSurname = new TextField("Surname","Enter your surname");
        TextField txtPhoneNumber = new TextField("Phone Number","Enter your phone number");



        binder.bind(txtName,Person::getName,Person::setName);
        binder.bind(txtSurname,Person::getSurname,Person::setSurname);
        binder.bind(txtPhoneNumber,Person::getPhoneNumber,Person::setPhoneNumber);

        FormLayout formLayout = new FormLayout();
        formLayout.add(txtName,txtSurname,txtPhoneNumber);

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.setSpacing(true);


        Button btnSave = new Button("Save");
        Button btnCancel = new Button("Cancel");
        btnCancel.addClickListener(buttonClickEvent -> {
            dialogPerson.close();
        });

        btnSave.addClickListener(buttonClickEvent -> {

            Person person = new Person();
            try {
                binder.writeBean(person);
            } catch (ValidationException e) {
                e.printStackTrace();
            }

            person.setId(itemIdForEdition);
            SystemUser loggedInSystemUser=new SystemUser();
            loggedInSystemUser.setId(loggedInSystemUserId);

            person.setSystemUser(loggedInSystemUser);

            personService.save(person);
            refreshData(txtFilter.getValue().toString());
            dialogPerson.close();
        });


        horizontalLayout.add(btnCancel,btnSave);
        dialogPerson.add(new H3("New Person"),formLayout,horizontalLayout);

        btnNew.addClickListener(buttonClickEvent -> {
            itemIdForEdition=0L;
            binder.readBean(new Person());
            dialogPerson.open();
        });

        refreshData(txtFilter.getValue().toString());

        grid.removeColumnByKey("id");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setColumns("name", "surname", "phoneNumber");
        grid.addComponentColumn(item -> createRemoveButton(grid, item))
                .setHeader("Actions");


        Button btnLogOut = new Button("LogOut");
        btnLogOut.addClickListener(buttonClickEvent -> {
            VaadinSession.getCurrent().getSession().invalidate();
            UI.getCurrent().getPage().setLocation("/login");
        });

        add(new H2("Phone List"),btnLogOut,btnNew,filterGroup,grid);



    }



    private void refreshData(String filter){
        List<Person> personList = new ArrayList<>();
        personList.addAll(personService.getList(filter,loggedInSystemUserId));
        grid.setItems(personList);
    }

    private void onDelete(ConfirmDialog.ConfirmEvent confirmEvent) {

    }

    private HorizontalLayout createRemoveButton(Grid<Person> grid, Person item) {
        @SuppressWarnings("unchecked")
        Button btnDelete = new Button("Delete");
        btnDelete.addClickListener(buttonClickEvent -> {
            //Notification.show("Delete item clicked on :" + item.getName());

            ConfirmDialog dialog = new ConfirmDialog("Confirm delete",
                    "Are you sure you want to delete this record?", "Delete", confirmEvent -> {
                    personService.delete(item);
                    refreshData(txtFilter.getValue().toString());
            },
                    "Cancel", cancelEvent -> {

            });
            dialog.setConfirmButtonTheme("error primary");

            dialog.open();
        });

        Button btnUpdate = new Button("Update");
        btnUpdate.addClickListener(buttonClickEvent -> {
            itemIdForEdition=item.getId();
            binder.readBean(item);
            dialogPerson.open();
        });

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(btnUpdate,btnDelete);

        return horizontalLayout;
    }



}
