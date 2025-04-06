package com.logistics.order_generator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Random;

@Component
public class MessageProducer {
    private final Logger LOGGER = LoggerFactory.getLogger(MessageProducer.class);
    private KafkaTemplate<String, String> kafkaTemplate;
    private int MESSAGE_COUNT = 1;
    private ObjectMapper objectMapper;

    @Value("${spring.kafka.topic}")
    private String TOPIC;

    public MessageProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    private Order generateRandomOrder(){
        byte[] array = new byte[7];
        new Random().nextBytes(array);
        String productName = new String(array, StandardCharsets.UTF_8);
        new Random().nextBytes(array);
        String description = new String(array, StandardCharsets.UTF_8);
        new Random().nextBytes(array);
        String destination = new String(array, StandardCharsets.UTF_8);
        new Random().nextBytes(array);
        String buyerName = new String(array, StandardCharsets.UTF_8);

        return new Order(MESSAGE_COUNT, productName, description, destination, buyerName, LocalDateTime.now());
    }

    private String convertOrderToJsonString(Order order){
        String orderJsonString;
        try{
            orderJsonString = objectMapper.writeValueAsString(order);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return orderJsonString;
    }

    @Scheduled(fixedRate = 10000)
    public void publishMessage(){
        try{
            LOGGER.info("\n\nPublishing message {} to topic {}", MESSAGE_COUNT, TOPIC);
            Order newOrder = generateRandomOrder();
            String newOrderJsonString = convertOrderToJsonString(newOrder);
            kafkaTemplate.send(TOPIC, newOrderJsonString);
            MESSAGE_COUNT++;
            LOGGER.info("Finished publishing message {} to topic {}", newOrderJsonString, TOPIC);
        } catch (Exception e){
            LOGGER.error("Encountered error while trying to publish to topic {}: ", TOPIC, e);
        }

    }
}
