package com.company.base.endpoint.event.model;

import static java.lang.Math.random;

import com.company.base.PojaGenerated;
import com.company.base.endpoint.event.EventStack;
import java.time.Duration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@PojaGenerated
@NoArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
@AllArgsConstructor
@Builder
public class DurablyFallibleUuidCreated extends PojaEvent {
  private UuidCreated uuidCreated;
  private int waitDurationBeforeConsumingInSeconds;
  private double failureRate;

  public boolean shouldFail() {
    return random() < failureRate;
  }

  @Override
  public Duration maxConsumerDuration() {
    return Duration.ofSeconds(
        waitDurationBeforeConsumingInSeconds + uuidCreated.maxConsumerDuration().toSeconds());
  }

  @Override
  public Duration maxConsumerBackoffBetweenRetries() {
    return uuidCreated.maxConsumerBackoffBetweenRetries();
  }

  @Override
  public EventStack getEventStack() {
    return EventStack.EVENT_STACK_2;
  }
}
