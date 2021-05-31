package com.example.application.services;

import com.example.application.models.Person;
import java.util.Set;

public interface PersonService {
    Set<Person> getList();
    Person save (Person p);
}
