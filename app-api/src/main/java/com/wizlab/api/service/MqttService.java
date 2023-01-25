package com.wizlab.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.wizlab.common.domain.TpmsDto;
import com.wizlab.common.domain.TpmsEntity;
import com.wizlab.common.repository.MqttRepository;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MqttService {
    
    private final MqttRepository mqttRepository;
    private final ModelMapper modelMapper;

    public TpmsDto findById(final Long id) {
        final Optional<TpmsEntity> entity = mqttRepository.findById(id);
        modelMapper.typeMap(TpmsEntity.class, TpmsDto.class)
                    .addMappings(mapper -> {
                        mapper.map(TpmsEntity::getId, TpmsDto::setId);
                        mapper.map(TpmsEntity::getHeader, TpmsDto::setHeader);
                        mapper.map(TpmsEntity::getEntry, TpmsDto::setEntry);
                        mapper.map(TpmsEntity::getData, TpmsDto::setData);
                    });
        TpmsDto tpmsDto = modelMapper.map(entity.get(), TpmsDto.class);
        log.info("Header controllerID = {}", tpmsDto.getHeader().getControllerId());
        
        return tpmsDto;
    }

    public List<TpmsDto> findAll() {
        final List<TpmsEntity> tpms = mqttRepository.findAll();
        final List<TpmsDto> tpmsDto = tpms.stream()
                                    .map(t -> modelMapper.map(t, TpmsDto.class))
                                    .collect(toList());
        
        return tpmsDto;
    }
}
