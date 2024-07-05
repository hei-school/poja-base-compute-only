package com.company.base.endpoint.event.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Duration;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class SendEmailTriggered extends PojaEvent {
  @JsonProperty("recipients")
  private List<String> recipients;

  @JsonProperty("message")
  private String message;

  @JsonProperty("object")
  private String object;

  @Override
  public Duration maxConsumerDuration() {
    return Duration.ofMinutes(3);
  }

  @Override
  public Duration maxConsumerBackoffBetweenRetries() {
    return Duration.ofMinutes(1);
  }
}
