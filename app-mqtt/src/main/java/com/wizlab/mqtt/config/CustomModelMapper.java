package com.wizlab.mqtt.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;

// MatchingStrategies.STANDARD(default)
//  - source와 destination 속성과 지능적으로 일치 시킴
//  - 모든 destination 객체의 property 토큰들은 매칭되어야 한다.
//  - 모든 source 객체의 property들은 하나 이상의 토큰이 매칭되어야 한다.
//  - 대부분의 상황에 사용 가능
//  ex) appleBanana ==> apple & banana == banana & apple
// MatchingStrategies.STRICT
//  - 가장 엄격한 전략
//  - source와 destination의 타입과 필드명이 같을 때만 변환
//  - 의도하지 않은 매핑이 일어나는 것을 방지할 때 사용
// MatchingStrategies.LOOSE
//  - 가장 느슨한 전략
//  - 계층 구조의 마지막 destination 필드만 일치하도록 하여 source 필드를 destination 필드에 매핑할 수 있음
//  - 토큰을 어떤 순서로도 일치 시킬 수 있음
//  - 마지막 destination 필드명은 모든 토큰이 일치해야한다.
//  - 마지막 source 필드명에는 일치하는 토큰이 하나 이상 있어야 한다.
//  - 의도하지 않은 매핑이 될 확률이 높아 잘 사용하지 않는다.
// Object.class -> String: String text = writeValueAsString(instance of Object.class);
// String -> Object.class: Object obj = readValue(text, Object.class)
public class CustomModelMapper {
    
    // ModelMapper 의 AccessLevel 의 기본값은 PUBLIC 이며, 메소드나 필드가 public 일 때 자동 매핑 대상이 되는 것이며,
    // private 필드를 자동 매핑되도록 하려면 AccessLevel 을 PRIVATE 로 변경하면 된다
    public static ModelMapper mapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldAccessLevel(AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true);
        return modelMapper;
    }
}
