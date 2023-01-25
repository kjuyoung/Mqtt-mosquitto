package com.wizlab.common.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.*;
// import jakarta.persistence.*;

@Getter
@Entity
@Table(name = "tpms_table")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TpmsEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Header header;
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tpms_entity_entry", joinColumns = @JoinColumn(name = "tpms_entity_id", referencedColumnName = "id"))
    private List<String> entry = new ArrayList<>();
    
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tpms_entity_data", joinColumns = @JoinColumn(name = "tpms_entity_id", referencedColumnName = "id"))
    private Map<String, Attribute> data = new HashMap<>();

    @Builder
    public TpmsEntity(Header header, List<String> entry, Map<String, Attribute> data) {
        this.header = header;
        this.entry = entry;
        this.data = data;
    }

    public void saveControllerId(String controllerId) {
        this.header.saveControllerId(controllerId);
    }

    public void saveAttribute(Map<String, Attribute> data) {
        this.data = data;
    }
}
