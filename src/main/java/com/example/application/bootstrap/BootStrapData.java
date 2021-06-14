package com.example.application.bootstrap;

import com.example.application.models.Person;
import com.example.application.models.SystemUser;
import com.example.application.services.PersonService;
import com.example.application.services.SystemUserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BootStrapData implements CommandLineRunner {


    private final PersonService personService;
    private final SystemUserService systemUserService;

    public BootStrapData(PersonService personService,SystemUserService systemUserService) {
        this.personService = personService;
        this.systemUserService=systemUserService;
    }


    @Override
    public void run(String... args) throws Exception {

        SystemUser systemUser=new SystemUser();
        systemUser.setEmail("cem@trakya.edu.tr");
        systemUser.setPassword("12345");
        systemUserService.save(systemUser);

        SystemUser systemUser2=new SystemUser();
        systemUser2.setEmail("ali@trakya.edu.tr");
        systemUser2.setPassword("12345");
        systemUserService.save(systemUser2);

        Person person=new Person();
        person.setName("ALİ");
        person.setSurname("DURU");
        person.setPhoneNumber("0544 444 4444");
        person.setSystemUser(systemUser);


        personService.save(person);

        Person person2=new Person();
        person2.setName("ALİYE");
        person2.setSurname("DURU");
        person2.setPhoneNumber("0544 555 5555");
        person2.setSystemUser(systemUser);
        personService.save(person2);

        Person person3=new Person();
        person3.setName("CEM");
        person3.setSurname("TASKIN");
        person3.setPhoneNumber("0544 555 5555");
        person3.setSystemUser(systemUser2);
        personService.save(person3);


    }
}
