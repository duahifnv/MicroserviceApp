package com.fizalise.notificationservice.service;

import com.fizalise.orderservice.event.OrderPlacedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public record NotificationService(JavaMailSender mailSender) {
    @KafkaListener(topics = "order-placed")
    public void listenTopic(OrderPlacedEvent orderPlacedEvent) {
        log.info("Got message from order-placed topic: {}", orderPlacedEvent);
        MimeMessagePreparator messagePreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage);
            messageHelper.setFrom("springshop@email.com");
            messageHelper.setTo(orderPlacedEvent.getEmail().toString());
            messageHelper.setSubject("Order with order number %s is placed successfully"
                    .formatted(orderPlacedEvent.getOrderNumber()));
            messageHelper.setText("""
                    Hi, %s %s
                    
                    Your order with order number %s is placed successfully.
                    
                    Best Regards
                    Spring Shop
                    """.formatted(orderPlacedEvent.getFirstName(),
                    orderPlacedEvent.getLastName(), orderPlacedEvent.getOrderNumber())
            );
        };
        try {
            mailSender.send(messagePreparator);
            log.info("Order notification sent to email {}", orderPlacedEvent.getEmail());
        } catch (MailException e) {
            throw new RuntimeException("Exception while sending mail from springshop@email.com", e);
        }
    }
}
