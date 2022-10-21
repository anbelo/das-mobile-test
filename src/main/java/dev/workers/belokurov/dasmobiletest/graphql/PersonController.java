package dev.workers.belokurov.dasmobiletest.graphql;

import dev.workers.belokurov.dasmobiletest.entities.Person;
import dev.workers.belokurov.dasmobiletest.entities.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.stream.StreamSupport;

@Controller
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @QueryMapping
    public Person findPersonByLastName(@Argument String lastName) {
        return StreamSupport
                .stream(personService.getAllPersons().spliterator(), false)
                .filter(person -> person.getLastName().equals(lastName))
                .findAny()
                .orElseThrow();
    }

    @QueryMapping
    public Iterable<Person> findAll() {
        return personService.getAllPersons();
    }

    @MutationMapping
    public Person createPerson(@Argument String firstName, @Argument String lastName) {
        return personService.createPerson(firstName, lastName);
    }

    @QueryMapping
    public Person readPerson(@Argument long id) {
        return personService.getPerson(id);
    }

    @MutationMapping
    public Person updatePerson(@Argument long id, @Argument String firstName, @Argument String lastName) {
        return personService.updatePerson(id, firstName, lastName);
    }

    @MutationMapping
    public boolean deletePerson(@Argument long id) {
        return personService.deletePerson(id);
    }

}
