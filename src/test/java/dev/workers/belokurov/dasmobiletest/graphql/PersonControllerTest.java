package dev.workers.belokurov.dasmobiletest.graphql;

import dev.workers.belokurov.dasmobiletest.entities.Person;
import dev.workers.belokurov.dasmobiletest.entities.PersonService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.graphql.test.tester.GraphQlTester;

import java.util.ArrayList;
import java.util.List;


@GraphQlTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockBean
    private PersonService personService;

    private final List<Person> people = new ArrayList<>();

    @BeforeEach
    void setUp() {
        people.add(new Person(1, "Stephen", "King"));
        people.add(new Person(2, "Charles", "Dickens"));
        people.add(new Person(3, "George", "Orwell"));
        people.add(new Person(4, "Ernest", "Hemingway"));
    }

    @AfterEach
    void tearDown() {
        people.clear();
    }

    @Test
    void findPersonByLastName() {
        String document = """
                    query {
                        findPersonByLastName(lastName: "Orwell") {
                            id,
                            firstName,
                            lastName
                        }
                    }
                """;

        Mockito.when(personService.getAllPersons()).thenReturn(people);

        graphQlTester.document(document)
                .execute()
                .path("findPersonByLastName")
                .entity(Person.class)
                .isEqualTo(people.get(2));
    }

    @Test
    void findAll() {
        String document = """
                    query {
                        findAll {
                            id,
                            firstName,
                            lastName
                        }
                    }
                """;

        Mockito.when(personService.getAllPersons()).thenReturn(people);

        graphQlTester.document(document)
                .execute()
                .path("findAll")
                .entityList(Person.class)
                .hasSize(4);
    }

    @Test
    void createPerson() {
        String document = """
                    mutation {
                        createPerson (
                            firstName: "Victor",
                            lastName: "Hugo"
                        ) {
                            id,
                            firstName,
                            lastName
                        }
                    }
                    """;
        Mockito.when(personService.createPerson("Victor", "Hugo")).thenReturn(new Person("Victor", "Hugo"));

        graphQlTester.document(document)
                .execute()
                .path("createPerson")
                .entity(Person.class)
                .isEqualTo(new Person( "Victor", "Hugo"));

    }

    @Test
    void readPerson() {
        String document = """
                    query {
                        readPerson(id: 2) {
                            id,
                            firstName,
                            lastName
                        }
                    }
                """;
        Mockito.when(personService.getPerson(2)).thenReturn(people.get(1));

        graphQlTester.document(document)
                .execute()
                .path("readPerson")
                .entity(Person.class)
                .isEqualTo(people.get(1));
    }

    @Test
    void updatePerson() {
        String document = """
                    mutation {
                        updatePerson(
                            id: 2,
                            lastName: "Bukowski"
                        ) {
                            id,
                            firstName,
                            lastName
                        }
                    }
                    """;

        people.get(1).setLastName("Bukowski");
        Mockito.when(personService.updatePerson(2, null, "Bukowski")).thenReturn(people.get(1));

        graphQlTester.document(document)
                .execute()
                .path("updatePerson")
                .entity(Person.class)
                .isEqualTo(people.get(1));
    }

    @Test
    void deletePerson() {
        String document = """
                    mutation {
                        deletePerson(id: 2)
                    }
                    """;
        Mockito.when(personService.getAllPersons()).thenReturn(people);
        Mockito.when(personService.deletePerson(2)).thenReturn(true);
        people.remove(1);

        graphQlTester.document(document)
                .execute()
                .path("deletePerson")
                .entity(Boolean.class)
                .isEqualTo(true);

        document = """
                    query {
                        findAll {
                            id,
                            firstName,
                            lastName
                        }
                    }
                """;

        graphQlTester.document(document)
                .execute()
                .path("findAll")
                .entityList(Person.class)
                .hasSize(3);
    }

}