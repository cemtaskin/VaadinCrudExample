package com.example.application.services;

import com.example.application.models.Person;
import com.example.application.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class PersonServiceImpl implements PersonService{

    private final PersonRepository personRepository;

    public PersonServiceImpl(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }


    @Override
    public Set<Person> getList() {
        Set<Person> personSet =new HashSet<>();
        personRepository.findAll().iterator().forEachRemaining(personSet::add);
        return personSet;
    }

    @Override
    public Set<Person> getList(String filter,Long systemUserId) {
        Set<Person> personSet =new HashSet<>();
        personRepository.findByNameContainingAndSystemUserId(filter,systemUserId).iterator().forEachRemaining(personSet::add);
        return personSet;
    }

    @Override
    public Person save(Person p) {
        return personRepository.save(p);
    }

    @Override
    public void delete(Person p) {
        personRepository.delete(p);
    }
}
