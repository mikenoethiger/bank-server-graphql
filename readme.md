[protocol](https://github.com/mikenoethiger/bank-server-socket#protocol) | [bank-client](https://github.com/mikenoethiger/bank-client) | [bank-server-socket](https://github.com/mikenoethiger/bank-server-socket) | [bank-server-graphql](https://github.com/mikenoethiger/bank-server-graphql) | [bank-server-rabbitmq](https://github.com/mikenoethiger/bank-server-rabbitmq)

# About

GraphQL implementation of the bank server backend. Spring boot is used as an HTTP container. The client counterpart can be found [here](https://github.com/mikenoethiger/bank-client/tree/master/src/main/java/bank/graphql).

# Run

From your IDE, run `bank.BankApplication` as a java application.

Or with gradle:

```bash
$ gradle bootRun
```

> By default the server runs on port `5002`, this can be changed in the `src/main/resources/application.properties` file.

# Endpoints

Some useful endpoints are:

* GraphiQL Client: `localhost:5002/graphiql`
* Altair Client: `localhost:5002/altair`
* Graph representation of the API: `localhost:5002/voyager`