package com.company.base.service.event;

import com.company.base.endpoint.event.model.SendEmailTriggered;
import com.company.base.endpoint.rest.controller.health.HealthEmailController;
import jakarta.mail.internet.AddressException;
import java.io.IOException;
import java.util.function.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendEmailTriggeredService implements Consumer<SendEmailTriggered> {
  @Autowired private HealthEmailController controller;

  @Override
  public void accept(SendEmailTriggered sendEmailTriggered) {
    log.info("Send email triggered: {}", sendEmailTriggered);
    sendEmailTriggered
        .getRecipients()
        .forEach(
            to -> {
              try {
                controller.send_emails(to);
              } catch (AddressException | IOException e) {
                throw new RuntimeException(e);
              }
            });
    log.info("Email sent");
  }
}
