package com.wizlab.api.controller;

import com.wizlab.api.domain.*;
import com.wizlab.api.service.MqttService;
import com.wizlab.common.domain.TpmsDto;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import javax.validation.Valid;
// import jakarta.validation.Valid;

@Slf4j
@Api(tags = "Common API")
@RestController
@RequiredArgsConstructor
public class ApiController {

    private final MqttService mqttService;
    private final ModelAssembler modelAssembler;

    @GetMapping("/tpms/{id}")
    public ResponseEntity<?> findOne(@Valid @PathVariable final Long id) {
        TpmsDto tpmsDto = mqttService.findById(id);
        return ResponseEntity.ok(modelAssembler.toModel(tpmsDto));
    }

    @GetMapping("/tpms")
    public ResponseEntity<?> findAll() {
        List<TpmsDto> tpmsDtos = mqttService.findAll();
        return ResponseEntity.ok(modelAssembler.toCollectionModel(tpmsDtos));
    }
}
