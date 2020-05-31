package com.kvsurii.examples.streams;

import com.kvsurii.examples.streams.wordcount.WordCountExample;
import io.dropwizard.Application;
import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;

public class KafkaStreamsApp extends Application<Configuration> {

  public static void main(String[] args) throws Exception {
    new KafkaStreamsApp().run(args);
  }

  @Override
  public String getName() {
    return "kafka-streams";
  }

  @Override
  public void run(Configuration configuration, Environment environment) throws Exception {
    final WordCountExample wordCountExample = new WordCountExample();
    final KafkaStreamsManager kafkaStreamsManager = new KafkaStreamsManager(wordCountExample);
    environment.lifecycle().manage(kafkaStreamsManager);
  }
}
