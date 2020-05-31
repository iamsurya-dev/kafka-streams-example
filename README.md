# Introduction
This Kafka Streams application serves as an example to write basic kafka-streams code in Dropwizard.

# Running in Local
Start zookeeper and kafka in local
```
bin/zookeeper-server-start.sh config/zookeeper.properties
bin/kafka-server-start.sh config/server.properties
```

To run in local
```
java -jar target/kafka-streams-1.0-SNAPSHOT.jar server src/main/resources/config.yml
```

Running Kafka Producer
```
bin/kafka-console-producer.sh --bootstrap-server localhost:9092 --topic input-topic
```

Running Kafka Consumer
```
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic output-topic
```
