package com.wizlab.api.domain;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.stereotype.Component;

import com.wizlab.api.controller.ApiController;
import com.wizlab.common.domain.TpmsDto;

@Component
public class ModelAssembler implements RepresentationModelAssembler<TpmsDto, EntityModel<TpmsDto>> {
    
    @Override
    public EntityModel<TpmsDto> toModel(TpmsDto tpmsDto) {
        return EntityModel.of(tpmsDto)
                        .add(linkTo(methodOn(ApiController.class).findOne(tpmsDto.getId())).withSelfRel())
                        .add(linkTo(methodOn(ApiController.class).findAll()).withRel("tpms"));
    }

    @Override
    public CollectionModel<EntityModel<TpmsDto>> toCollectionModel(Iterable<? extends TpmsDto> tpmsDtos) {
        return RepresentationModelAssembler.super.toCollectionModel(tpmsDtos)
                .add(linkTo(methodOn(ApiController.class).findAll()).withSelfRel());
    }
}
