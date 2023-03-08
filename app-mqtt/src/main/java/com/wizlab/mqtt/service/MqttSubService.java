package com.wizlab.mqtt.service;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wizlab.common.domain.TpmsEntity;
import com.wizlab.common.repository.MqttRepository;

@Service
@RequiredArgsConstructor
public class MqttSubService {

    private final MqttRepository mqttRepository;

    @Transactional
    public void saveTpms(TpmsEntity tpms) {
        mqttRepository.save(tpms);
    }
}
