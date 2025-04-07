package com.logistics.order_generator.configuration;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class H2DBConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(H2DBConfiguration.class);
    @Autowired
    JdbcTemplate jdbcTemplate;

    @PostConstruct
    public void initH2DB(){
        try{
            jdbcTemplate.execute("drop table orders if exists");
//            jdbcTemplate.execute("create table employees(id serial,first_name varchar(255),last_name varchar(255))");
            jdbcTemplate.execute("create table orders(order_id INTEGER, product_name varchar(20), description varchar(20), destination varchar(20), buyer_name varchar(20), order_placed_timestamp timestamp)");
        } catch (Exception e){
            LOGGER.error("\n\nUnable to create schema for H2 database with error: \n\n", e);
            throw new RuntimeException();
        }
    }
}
