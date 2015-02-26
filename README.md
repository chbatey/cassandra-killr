## Cassandra Killr

Example of how to make a real Cassandra cluster fail for acceptance and performance tests.

To build:

```
mvn clean package
```

This will build the following file: ```target/cassandra-killr-1.0-SNAPSHOT.jar```

Add this to your Cassandra node classpath e.g put it in the lib directory.

Then start Cassandra with the following option when starting Cassandra: ```-Dcassandra.custom_query_handler_class=info.batey.cassandra.breaker.KillrQueryHandler```