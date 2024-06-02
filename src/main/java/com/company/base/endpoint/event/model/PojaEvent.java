package com.company.base.endpoint.event.model;

import com.company.base.PojaGenerated;
import java.io.Serializable;
import java.time.Duration;

@PojaGenerated
public abstract class PojaEvent implements Serializable {
  public abstract Duration maxDuration();

  private Duration randomBackoffBetweenRetries() {
    return Duration.ofSeconds(maxBackoffBetweenRetries().toSeconds());
  }

  public abstract Duration maxBackoffBetweenRetries();

  public final Duration randomVisibilityTimeout() {
    var eventHandlerInitMaxDuration = Duration.ofSeconds(90); // note(init-visibility)
    return Duration.ofSeconds(
        eventHandlerInitMaxDuration.toSeconds()
            + maxDuration().toSeconds()
            + randomBackoffBetweenRetries().toSeconds());
  }
}
