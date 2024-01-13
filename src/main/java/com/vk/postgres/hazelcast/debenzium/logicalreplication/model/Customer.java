package com.vk.postgres.hazelcast.debenzium.logicalreplication.model;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.r2dbc.postgresql.codec.Json;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    @JsonProperty("test_json")
    private Json testJson;

    @JsonProperty("created_date")
    private Long createdDate;

}