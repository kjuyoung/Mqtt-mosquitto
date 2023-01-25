package com.wizlab.mqtt.config;

import java.util.Collections;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.InboundChannelAdapter;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.jpa.core.JpaExecutor;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.inbound.JpaPollingChannelAdapter;
import org.springframework.integration.jpa.outbound.JpaOutboundGateway;
import org.springframework.integration.jpa.support.JpaParameter;
import org.springframework.integration.jpa.support.OutboundGatewayType;
import org.springframework.integration.jpa.support.PersistMode;
import org.springframework.integration.annotation.Poller;
import org.springframework.messaging.MessageHandler;
import org.springframework.transaction.annotation.Transactional;

import com.wizlab.common.domain.TpmsEntity;

import javax.persistence.*;
// import jakarta.persistence.*;

@EntityScan(basePackageClasses = TpmsEntity.class)
@Configuration
@IntegrationComponentScan
public class JpaConfig {
    
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    /**
     *      Inbound
     * 
     *//*
    @Bean
    public JpaExecutor inboundJpaExecutor() {
        JpaExecutor executor = new JpaExecutor(this.entityManagerFactory);
        executor.setJpaQuery("from TpmsEntity t");
        return executor;
    }

    @Bean
    @InboundChannelAdapter(channel = "jpaInputChannel", poller = @Poller(fixedDelay = "${poller.interval:1000}"))
    public MessageSource<?> jpaInbound() {
        return new JpaPollingChannelAdapter(inboundJpaExecutor());
    }

    @Bean
    @ServiceActivator(inputChannel = "jpaInputChannel")
    public MessageHandler inputHandler() {
        return message -> System.out.println(message.getPayload());
    }
*/
    /**
     *      Persist
     * 
     */
    @MessagingGateway(name = "persistJpaGateway")
    interface persistJpaGateway {
       @Gateway(requestChannel = "createTpmsRequestChannel")
       @Transactional
       void createTpms(TpmsEntity payload);
    }

    @Bean
    public JpaExecutor outboundJpaExecutor() {
        JpaExecutor executor = new JpaExecutor(this.entityManagerFactory);
        executor.setEntityClass(TpmsEntity.class);
        executor.setPersistMode(PersistMode.PERSIST);
        return executor;
    }

    @Bean
    @ServiceActivator(inputChannel = "createTpmsRequestChannel")
    public MessageHandler persistJpaOutbound() {
        JpaOutboundGateway adapter = new JpaOutboundGateway(outboundJpaExecutor());
        adapter.setProducesReply(false);
        return adapter;
    }

    /**
     *      Update
     * 
     */
    /*
    @MessagingGateway
    interface updateJpaGateway {
       @Gateway(requestChannel = "jpaUpdateChannel")
       @Transactional
       void updateTpms(TpmsEntity payload);

    }

    @Bean
    @ServiceActivator(inputChannel = "jpaUpdateChannel")
    public MessageHandler updateJpaOutbound() {
        JpaOutboundGateway adapter = new JpaOutboundGateway(new JpaExecutor(this.entityManagerFactory));
        adapter.setOutputChannelName("updateResults");
        return adapter;
    }*/

    /**
     *      Retrieving
     * 
     */
    /*
    @Bean
    public JpaExecutor retrieveJpaExecutor() {
        JpaExecutor executor = new JpaExecutor(this.entityManagerFactory);
        executor.setJpaQuery("from TpmsEntity t where t.id = :id");
        executor.setJpaParameters(Collections.singletonList(new JpaParameter("id", null, "payload")));
        executor.setExpectSingleResult(true);
        return executor;
    }

    @Bean
    @ServiceActivator(inputChannel = "jpaRetrievingChannel")
    public MessageHandler jpaOutbound() {
        JpaOutboundGateway adapter = new JpaOutboundGateway(retrieveJpaExecutor());
        adapter.setOutputChannelName("retrieveResults");
        adapter.setGatewayType(OutboundGatewayType.RETRIEVING);
        return adapter;
    }
    */
}
