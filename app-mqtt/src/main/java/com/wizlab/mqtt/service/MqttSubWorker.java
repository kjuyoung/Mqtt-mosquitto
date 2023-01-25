package com.wizlab.mqtt.service;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.wizlab.mqtt.config.CustomModelMapper;
import com.wizlab.common.domain.Attribute;
import com.wizlab.common.domain.HeaderDto;
import com.wizlab.common.domain.TpmsEntity;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.jpa.dsl.Jpa;
import org.springframework.integration.jpa.support.PersistMode;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Slf4j
@Builder
@RequiredArgsConstructor
public class MqttSubWorker implements Runnable{

    private final BlockingQueue<String> queue;
    private final MqttSubService mqttSubService;
    @PersistenceUnit
    private final EntityManagerFactory entityManagerFactory;

    @Override
    public void run() {
        log.info("MqttSubWorker :: run");

        while(true) {

            try {
                String message = queue.take();

                String[] msgBlocks = message != null ? message.split("\\^") : new String[0];
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> json = new HashMap<String, Object>();
                Map<String, Attribute> tpmsDataMap = new HashMap<String, Attribute>();

                json = mapper.readValue(msgBlocks[1].toString(), new TypeReference<HashMap<String, Object>>(){});
                // log.info("mqtt json {}", json);
                TpmsEntity tpms = CustomModelMapper.mapper().map(json, TpmsEntity.class);
                
                for (String key : json.keySet()) {
                    // log.info("Before {}: {}", key, json.get(key));
                    if (!(key.equals("entry") || key.equals("header"))) {
                        Attribute tpmsData = CustomModelMapper.mapper().map(json.get(key), Attribute.class);
                        Object[] value = tpmsData.getValue();

                        ZonedDateTime current = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
                        String timestamp = current.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZZZ"));
                
                        tpmsData.setTime(timestamp);
                        tpmsData.setLatitude(tpms.getHeader().getLatitude());
                        tpmsData.setLongitude(tpms.getHeader().getLongitude());

                        tpmsData.setChipId(value[0].toString());
                        tpmsData.setTemperature(((Number)value[1]).doubleValue());
                        tpmsData.setVoltage(((Number)value[2]).doubleValue());
                        tpmsData.setPressure(((Number)value[3]).doubleValue());
                        tpmsData.setAccz(((Number)value[7]).doubleValue());
                        tpmsData.setT_patch0((Integer)value[4]);
                        tpmsData.setT_patch1((Integer)value[5]);
                        tpmsData.setT_period((Integer)value[6]);
                        tpmsData.setT_spl_period((Integer)value[8]);
                        tpmsData.setT_verified((Integer)value[9]);

                        tpmsDataMap.put(key, tpmsData);
                        tpms.saveAttribute(tpmsDataMap);
                    } 
                    else if(key.equals("header")) {
                        HeaderDto headerDto = mapper.convertValue(json.get(key), HeaderDto.class);
                        tpms.saveControllerId(headerDto.getId());
                    }
                }
                
                // for (String key : tpms.getData().keySet()) {
                //     Attribute tpmsData = tpms.getData().get(key);
                //     log.info("After {}: {}", key, tpms.getData().get(key));
                // }                
                // log.info("mqtt tpms sensor data {}", tpms);
                
                mqttSubService.saveTpms(tpms);

                // Jpa.outboundAdapter(entityManagerFactory)
                //     .usePayloadAsParameterSource(true)
                //     .jpaQuery("insert into tpms_table valuse("+tpms.getHeader().getControllerId()
                //         +","+tpms.getHeader().getLatitude()
                //         +","+tpms.getHeader().getLongitude()
                //         +","+tpms.getHeader().getTime()+")")
                //     .get();

            } catch (InterruptedException ie) {
                log.error("[ERROR] - InterruptedException :: {}", ie.getMessage(), ie);
                return;
            } catch (Exception e) {
                log.error("[ERROR] - Exception :: {}", e.getMessage(), e);
                return;
            }
        }
    }

    @Bean
    public IntegrationFlow outboundAdapterFlow() {
        return f -> f
                .handle(Jpa.outboundAdapter(this.entityManagerFactory)
                                .entityClass(TpmsEntity.class)
                                .persistMode(PersistMode.PERSIST),
                        e -> e.transactional());
    }
}
