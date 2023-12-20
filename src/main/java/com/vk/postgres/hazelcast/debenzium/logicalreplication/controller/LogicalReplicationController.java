package com.vk.postgres.hazelcast.debenzium.logicalreplication.controller;

import com.hazelcast.core.HazelcastInstance;
import com.vk.postgres.hazelcast.debenzium.logicalreplication.model.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
public class LogicalReplicationController {

    @Autowired
    private HazelcastInstance hazelcastInstance;


    @GetMapping
    public Flux<Customer> checkCache(){
        return Flux.fromIterable(hazelcastInstance.<Integer, Customer>getMap("customers").values());
    }

}
