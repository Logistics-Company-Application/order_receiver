package com.logistics.order_generator.controllers;

import com.logistics.order_generator.models.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/h2")
public class H2DBController {
    private static final String FIND_ALL_ORDERS_SQL = "select * from orders";
    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping("/findAll")
    public List<OrderDTO> findAll(){
        return jdbcTemplate.query(FIND_ALL_ORDERS_SQL, new OrderDTO.OrderDTOMapper());
    }
}
