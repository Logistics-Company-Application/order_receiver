package com.logistics.order_generator.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.order_generator.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private ObjectMapper objectMapper;

    public MessageConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private Order convertJsonStringToOrder(String orderJsonString){
        Order newOrder;
        try{
            newOrder = objectMapper.readValue(orderJsonString, Order.class);
        } catch (JsonMappingException e) {
            LOGGER.error("\n\nEncountered JsonMappingException while trying to map new order string to Order object: \n\n", e);
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            LOGGER.error("\n\nEncountered JsonProcessingException while trying to map new order string to Order object: \n\n", e);
            throw new RuntimeException(e);
        } catch (Exception e){
            LOGGER.error("\n\nEncountered unexpected exception while trying to map new order string to Order object: \n\n", e);
            throw new RuntimeException(e);
        }
        return newOrder;
    }

    @KafkaListener(topics = "topic-1", groupId = "group-id")
    public void listenToTopic(String orderJsonString){
        Order newOrder = convertJsonStringToOrder(orderJsonString);
        LOGGER.info("\n\nReceived new order with id {}, productName of {}, description of {}, destination of {}, buyername of {} and placed at {}\n\n", newOrder.getOrderId(), newOrder.getProductName(), newOrder.getDescription(), newOrder.getDestination(), newOrder.getBuyerName(), newOrder.getOrderPlacedTimestamp());
    }
}
