package com.kvsurii.examples.streams;

import com.kvsurii.examples.streams.wordcount.WordCountExample;
import io.dropwizard.lifecycle.Managed;

public class KafkaStreamsManager implements Managed {

  private WordCountExample wordCountExample;

  public KafkaStreamsManager(WordCountExample wordCountExample) {
    this.wordCountExample = wordCountExample;
  }

  @Override
  public void start() throws Exception {
    wordCountExample.start();
  }

  @Override
  public void stop() throws Exception {
    wordCountExample.stop();
  }
}
