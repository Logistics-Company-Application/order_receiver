package com.logistics.order_generator.models;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class OrderDTO {
    private int orderId;
    private String productName;
    private String description;
    private String destination;
    private String buyerName;
    private LocalDateTime orderPlacedTimestamp;

    public static class OrderDTOMapper implements RowMapper<OrderDTO> {
        @Override
        public OrderDTO mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            return OrderDTO.builder()
                    .orderId(resultSet.getInt("order_id"))
                    .productName(resultSet.getString("product_name"))
                    .description(resultSet.getString("description"))
                    .destination(resultSet.getString("destination"))
                    .buyerName(resultSet.getString("buyer_name"))
                    .orderPlacedTimestamp(resultSet.getObject("order_placed_timestamp", LocalDateTime.class))
                    .build();
        }
    }
}
