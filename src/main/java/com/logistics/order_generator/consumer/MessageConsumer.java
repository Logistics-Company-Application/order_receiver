package com.logistics.order_generator.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.logistics.order_generator.models.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Map;

@Component
public class MessageConsumer {
    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageConsumer.class);
    private ObjectMapper objectMapper;
    private static final String NEW_ORDER_SQL_INSERT_STATEMENT = """
            insert into orders(order_id, product_name, description, destination, buyer_name, order_placed_timestamp)
            values (?, ?, ?, ?, ?, ?)
            """;

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

    public void postNewOrderToDB(Order newOrder){
        try{
            jdbcTemplate.update(NEW_ORDER_SQL_INSERT_STATEMENT,
                    newOrder.getOrderId(),
                    newOrder.getProductName(),
                    newOrder.getDescription(),
                    newOrder.getDestination(),
                    newOrder.getBuyerName(),
                    newOrder.getOrderPlacedTimestamp()
            );
            LOGGER.info("Successfully published order {} to the H2 database", newOrder.getOrderId());
        } catch (Exception e){
            LOGGER.error("Encountered unexpected exception during attempted insert: ", e);
            throw new RuntimeException();
        }
    }

    @KafkaListener(topics = "topic-1", groupId = "group-id")
    public void listenToTopic(String orderJsonString){
        Order newOrder = convertJsonStringToOrder(orderJsonString);
        LOGGER.info("\n\nReceived new order with id {}, productName of {}, description of {}, destination of {}, buyername of {} and placed at {}\n\n", newOrder.getOrderId(), newOrder.getProductName(), newOrder.getDescription(), newOrder.getDestination(), newOrder.getBuyerName(), newOrder.getOrderPlacedTimestamp());
        postNewOrderToDB(newOrder);
    }
}
