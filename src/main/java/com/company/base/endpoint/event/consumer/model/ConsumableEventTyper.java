package com.company.base.endpoint.event.consumer.model;

import com.amazonaws.services.lambda.runtime.events.SQSEvent.SQSMessage;
import com.company.base.PojaGenerated;
import com.company.base.endpoint.event.EventConf;
import com.company.base.endpoint.event.model.PojaEvent;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.ChangeMessageVisibilityRequest;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;

@PojaGenerated
@Component
@Slf4j
@AllArgsConstructor
public class ConsumableEventTyper implements Function<List<SQSMessage>, List<ConsumableEvent>> {

  private static final String DETAIL_PROPERTY = "detail";
  private static final String DETAIL_TYPE_PROPERTY = "detail-type";

  private final ObjectMapper om;
  private final EventConf eventConf;

  @Override
  public List<ConsumableEvent> apply(List<SQSMessage> messages) {
    var res = new ArrayList<ConsumableEvent>();
    for (SQSMessage message : messages) {
      TypedEvent typedEvent;
      try {
        typedEvent = toTypedEvent(message);
      } catch (Exception e) {
        log.error(e.getMessage());
        log.error("Message could not be unmarshalled, message : {} \n", message);
        continue;
      }
      String sqsQueueUrl = typedEvent.payload().getEventStack().getSqsQueueUrl();
      ConsumableEvent consumableEvent =
          new ConsumableEvent(
              typedEvent, acknowledger(message, sqsQueueUrl), failer(message, sqsQueueUrl));
      res.add(consumableEvent);
    }
    return res;
  }

  @SneakyThrows
  private TypedEvent toTypedEvent(SQSMessage message) {
    TypeReference<Map<String, Object>> typeRef = new TypeReference<>() {};
    Map<String, Object> body = om.readValue(message.getBody(), typeRef);
    String typeName = body.get(DETAIL_TYPE_PROPERTY).toString();
    return new TypedEvent(
        typeName, (PojaEvent) om.convertValue(body.get(DETAIL_PROPERTY), Class.forName(typeName)));
  }

  private Runnable acknowledger(SQSMessage message, String sqsQueueUrl) {
    return () -> {
      sqsClient()
          .deleteMessage(
              DeleteMessageRequest.builder()
                  .queueUrl(sqsQueueUrl)
                  .receiptHandle(message.getReceiptHandle())
                  .build());
      log.info("deleted message: {}", message);
    };
  }

  private Runnable failer(SQSMessage message, String sqsQueueUrl) {
    return () -> {
      var newRandomVisibility =
          (int) (toTypedEvent(message).payload()).randomVisibilityTimeout().toSeconds();
      sqsClient()
          .changeMessageVisibility(
              ChangeMessageVisibilityRequest.builder()
                  .queueUrl(sqsQueueUrl)
                  .receiptHandle(message.getReceiptHandle())
                  .visibilityTimeout(newRandomVisibility)
                  .build());
      log.info("newVisibility={}, message={}", newRandomVisibility, message);
    };
  }

  private SqsClient sqsClient() {
    return eventConf.getSqsClient();
  }
}
