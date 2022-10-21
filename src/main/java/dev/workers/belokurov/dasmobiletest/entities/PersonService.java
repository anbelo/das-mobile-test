package dev.workers.belokurov.dasmobiletest.entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Iterable<Person> getAllPersons() {
        return personRepository.findAll();
    }

    public Person createPerson(String firstName, String lastName) {
        return personRepository.save(new Person(firstName, lastName));
    }

    public Person getPerson(long id) {
        return personRepository.findById(id).orElseThrow();
    }

    public Person updatePerson(long id, String firstName, String lastName) {
        Optional<Person> person = personRepository.findById(id);
        person.ifPresent(p -> {
            if (firstName != null) {
                p.setFirstName(firstName);
            }
            if (lastName != null) {
                p.setLastName(lastName);
            }
        });
        return personRepository.save(person.orElseThrow());
    }

    public boolean deletePerson(long id) {
        boolean res = personRepository.existsById(id);
        personRepository.deleteById(id);
        return res;
    }

}
