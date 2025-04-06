package com.logistics.order_generator;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class Order {
    private int orderId;
    private String productName;
    private String description;
    private String destination;
    private String buyerName;
    private LocalDateTime orderPlacedTimestamp;
}
