package com.wizlab.common.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class TpmsDto {      // used for app-api module

    private Long id;
    private Header header;
    private List<String> entry = new ArrayList<>();
    private Map<String, Attribute> data = new HashMap<>();

    @Builder
    public TpmsDto(Header header, List<String> entry, Map<String, Attribute> data) {
        this.header = header;
        this.entry = entry;
        this.data = data;
    }

    @Override
    public String toString() {
        return "TpmsDto{" +
                "header=" + header +
                ", entry=" + entry +
                ", data=" + data +
                '}';
    }
}
