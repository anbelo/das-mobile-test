# SpringBoot + GraphQl Test
### Usage
```bash
mvn clean package
docker-compose up -d
```
GraphiQL: http://localhost:8080/graphiql

http://localhost:8080/graphql

```graphql
type Person {
    id: ID!
    firstName: String
    lastName: String
}

type Query {
    readPerson(id: ID!) : Person
    findPersonByLastName(lastName: String!): Person
    findAll: [Person]!
}

type Mutation {
    createPerson(firstName: String, lastName: String) : Person!
    updatePerson(id: ID!, firstName: String, lastName: String) : Person!
    deletePerson(id: ID!) : Boolean!
}
```

