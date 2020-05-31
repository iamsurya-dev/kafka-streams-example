package com.kvsurii.examples.streams.wordcount;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.KStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordCountExample {

  private KafkaStreams kafkaStreams;
  private static final Logger log = LoggerFactory.getLogger(WordCountExample.class);

  private Topology createTopology(final String inputTopic, final String outputTopic) {
    final StreamsBuilder builder = new StreamsBuilder();
    KStream<String, String> source = builder.stream(inputTopic);

    final Pattern pattern = Pattern.compile("\\W+");
    KStream<String, String> result = source.filter((key, value) -> value != null)
        .flatMapValues(value -> Arrays.asList(pattern.split(value.toLowerCase())))
        .map((key, value) -> new KeyValue<>(value, value))
        .peek((key, value) -> log.info("Key is {} , Value is : {}", key, value));

    result.to(outputTopic);

    return builder.build();
  }

  private Properties createStreamSettings() {
    Properties props = new Properties();
    props.put(StreamsConfig.APPLICATION_ID_CONFIG, "wordcount-example");
    props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
    props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

    return props;
  }

  public void start() {
    Properties streamProperties = createStreamSettings();
    Topology topology = createTopology("input-topic", "output-topic");

    try {
      kafkaStreams = new KafkaStreams(topology, streamProperties);
      kafkaStreams.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
          throw new RuntimeException("Error starting stream :" + e);
        }
      });

      kafkaStreams.start();
    } catch (KafkaException e) {
      throw new RuntimeException("KafkaException :" + e);
    } catch (Exception e) {
      throw new RuntimeException("Error starting stream :" + e);
    }
  }

  public void stop() {
    kafkaStreams.close();
  }
}
