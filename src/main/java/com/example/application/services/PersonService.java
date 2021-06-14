package com.example.application.services;

import com.example.application.models.Person;
import java.util.Set;

public interface PersonService {
    Set<Person> getList();
    Set<Person> getList(String filter,Long systemUserId);
    Person save (Person p);
    void delete(Person p);
}
