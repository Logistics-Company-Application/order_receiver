package com.logistics.order_generator.controllers;

import com.logistics.order_generator.models.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/h2")
public class H2DBController {
    private static final String FIND_ALL_ORDERS_SQL = "select * from orders";
    private static final String FIND_ALL_ORDERS_BY_BUYER_SQL = "select * from orders where buyer_name = :buyerName";
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @GetMapping("/findAll")
    public List<OrderDTO> findAll(){
        return jdbcTemplate.query(FIND_ALL_ORDERS_SQL, new OrderDTO.OrderDTOMapper());
    }

    @GetMapping("/findAllByBuyer/{buyerName}")
    public List<OrderDTO> findAllByBuyer(@PathVariable("buyerName") String buyerName){
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("buyerName", buyerName);
        return jdbcTemplate.query(FIND_ALL_ORDERS_BY_BUYER_SQL, namedParameters, new OrderDTO.OrderDTOMapper());
    }
}
