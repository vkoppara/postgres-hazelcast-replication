package com.vk.postgres.hazelcast.debenzium.logicalreplication.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import io.r2dbc.postgresql.codec.Json;

import java.io.IOException;
import java.io.Serializable;

public  class JsonDeserializer1 extends JsonDeserializer<Json> implements Serializable {

    @Override
    public Json deserialize(JsonParser p, DeserializationContext ctxt) throws IOException{
        return Json.of(p.getValueAsString());
    }
}
