package com.vk.postgres.hazelcast.debenzium.logicalreplication.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.config.Config;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.cp.lock.FencedLock;
import com.hazelcast.instance.impl.BootstrappedInstanceProxyFactory;
import com.hazelcast.jet.cdc.CdcSinks;
import com.hazelcast.jet.cdc.ChangeRecord;
import com.hazelcast.jet.cdc.postgres.PostgresCdcSources;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.StreamSource;
import com.vk.postgres.hazelcast.debenzium.logicalreplication.model.Customer;
import io.r2dbc.postgresql.codec.Json;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hazelcast.jet.pipeline.Pipeline;

@Configuration
public class Table1DebeziumListener {

    @PostConstruct
    public void postConstruct(){

        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addDeserializer(Json.class, new JsonDeserializer1());
        objectMapper.registerModule(module);

        StreamSource<ChangeRecord> source = PostgresCdcSources.postgres("source")
                //.setDatabaseAddress("172.18.0.2")
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
                        r -> objectMapper.readValue(r.value().toJson(), Customer.class).toString()));

        JobConfig cfg = new JobConfig().setName("postgres-monitor");
        Config config = new Config();
        NetworkConfig networkConfig = config.getNetworkConfig();
        config.getJetConfig().setEnabled(true);
        /*networkConfig.getJoin()
                .getMulticastConfig()
                .setEnabled(true);*/
        /*networkConfig.getJoin()
                .getTcpIpConfig()
                .setEnabled(true);*/
        HazelcastInstance hz = BootstrappedInstanceProxyFactory.createWithCLIJetProxy(Hazelcast.newHazelcastInstance(config));

        FencedLock fencedLock = hz.getCPSubsystem().getLock("locked");
        System.out.println("isLocked"+fencedLock.isLocked());
        if(!fencedLock.isLocked()) {
            fencedLock.lock();
            hz.getJet().newJob(pipeline, cfg);
        }
    }

    @Bean
    public HazelcastInstance hazelcastInstance(){
        return HazelcastClient.newHazelcastClient();
    }


}
