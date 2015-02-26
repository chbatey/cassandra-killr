## Cassandra Killr

Example of how to make a real Cassandra cluster fail for acceptance and performance tests.

To build:

```mvn clean package```

This will build the following file:

```cassandra-killr-1.0-SNAPSHOT.jar```

Add this to your Cassandra node classpath e.g put it in the lib directory.

Then start Cassandra with the following option when starting Cassandra:

```-Dcassandra.custom_query_handler_class=info.batey.cassandra.breaker.KillrQueryHandler```

Killr Cassandra will then open up on port 9999 and accept the following two requests:

```
curl -X POST  localhost:9999/prime/reset
curl -X POST  localhost:9999/prime/readtimeout
```

Where localhost is where your Cassandra node is running.

Calling ```/prime/readtimeout``` will cause all queries to read time out, reset will go back to normal.