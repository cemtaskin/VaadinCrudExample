package com.example.application.views.main;

import com.example.application.models.Person;
import com.example.application.services.PersonService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route
public class MainView extends VerticalLayout {

    private final PersonService personService;
    Grid<Person> grid = new Grid<>(Person.class);

    public MainView(PersonService personService){
        this.personService = personService;

        Binder<Person> binder = new Binder<>();

        Button btnNew = new Button("Add", VaadinIcon.INSERT.create());

        Dialog dialog=new Dialog();
        dialog.setModal(true);

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
            dialog.close();
        });

        btnSave.addClickListener(buttonClickEvent -> {

            Person person = new Person();
            try {
                binder.writeBean(person);
            } catch (ValidationException e) {
                e.printStackTrace();
            }

            personService.save(person);
            refreshData();
            dialog.close();
        });


        horizontalLayout.add(btnCancel,btnSave);
        dialog.add(new H3("New Person"),formLayout,horizontalLayout);

        btnNew.addClickListener(buttonClickEvent -> {
            binder.readBean(new Person());
            dialog.open();
        });

        refreshData();

        grid.removeColumnByKey("id");
        grid.setSelectionMode(Grid.SelectionMode.NONE);
        grid.setColumns("name", "surname", "phoneNumber");
        grid.addComponentColumn(item -> createRemoveButton(grid, item))
                .setHeader("Actions");

        add(new H2("Person List"),btnNew,grid);
    }


    private void refreshData(){
        List<Person> personList = new ArrayList<>();
        personList.addAll(personService.getList());
        grid.setItems(personList);
    }

    private HorizontalLayout createRemoveButton(Grid<Person> grid, Person item) {
        @SuppressWarnings("unchecked")
        Button btnDelete = new Button("Delete");
        btnDelete.addClickListener(buttonClickEvent -> {
            Notification.show("Delete item clicked on :" + item.getName());
        });

        Button btnUpdate = new Button("Update");
        btnUpdate.addClickListener(buttonClickEvent -> {
            Notification.show("Update item clicked on :" + item.getName());
        });

        HorizontalLayout horizontalLayout=new HorizontalLayout();
        horizontalLayout.add(btnUpdate,btnDelete);

        return horizontalLayout;
    }

}
