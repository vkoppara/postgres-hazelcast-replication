package com.vk.postgres.hazelcast.debenzium.logicalreplication.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of={"email","firstName", "id", "lastName"})
@ToString
public class Customer implements Serializable {

    @JsonProperty("id")
    private int id;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @JsonProperty("email")
    private String email;


}