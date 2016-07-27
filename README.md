# magicbus

This is a very simple http to kafka gateway, written using Spring Boot.

     GET http://yourhost:8080/mb/topic-name

Will produce a message containing a timestamp and a few properties on the kafka topic **topic-name**

    POST http://yourhost:8080/mb/topic-name

Will produce a message consisting of the request body and a wrapper on topic-name

After building, run with:

     java magicbus.jar <path to properties file> [partition #]

If you pass in [partition #] magicbus will write to that partition of any topic that is requested.

The properties file contains:

    default.topic=lost-and-found
    kafka.server=localhost:9092


default.topic is not used yet.

kafka.server points to the initial kafka server for connection & discovery of cluster hosts.  This is just plugged into the bootstrap.servers property of the producer so a comma-separated list of host:port should work here too.
