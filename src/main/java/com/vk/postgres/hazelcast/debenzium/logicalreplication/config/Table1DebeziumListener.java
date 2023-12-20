package com.vk.postgres.hazelcast.debenzium.logicalreplication.config;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.jet.cdc.CdcSinks;
import com.hazelcast.jet.cdc.ChangeRecord;
import com.hazelcast.jet.cdc.postgres.PostgresCdcSources;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.StreamSource;
import com.vk.postgres.hazelcast.debenzium.logicalreplication.model.Customer;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import com.hazelcast.jet.pipeline.Pipeline;

@Configuration
public class Table1DebeziumListener {

    @PostConstruct
    public void postConstruct(){
        StreamSource<ChangeRecord> source = PostgresCdcSources.postgres("source")
                .setDatabaseAddress("127.0.0.1")
                .setDatabasePort(5432)
                .setDatabaseUser("vkoppara")
                .setDatabasePassword("password")
                .setDatabaseName("postgres")
                .setTableWhitelist("public.customer")
                .setLogicalDecodingPlugIn("pgoutput")
                .build();

        Pipeline pipeline = Pipeline.create();
        pipeline.readFrom(source)
                .withoutTimestamps()
                .peek()
                .writeTo(CdcSinks.map("customers",
                        r -> r.key().toMap().get("id"),
                        r -> r.value().toObject(Customer.class).toString()));

        JobConfig cfg = new JobConfig().setName("postgres-monitor");
        HazelcastInstance hz = Hazelcast.bootstrappedInstance();
        hz.getJet().newJob(pipeline, cfg);
    }

    @Bean
    public HazelcastInstance hazelcastInstance(){
        return HazelcastClient.newHazelcastClient();
    }


}
