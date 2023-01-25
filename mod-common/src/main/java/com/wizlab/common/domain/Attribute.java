package com.wizlab.common.domain;

import javax.persistence.*;
// import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class Attribute {

    @Transient
    private String[] attrib;  // chipid,    temperature, voltage, pressure, t_patch0, t_patch1, t_period,     accz, t_spl_period, t_verified
    @Transient
    private Object[] value;   // "ff3611e2",         22,   2.980,        1,        0,        0,        0,   -0.900,            0,        176

    // Header
    private String controllerId;        // Vehicle Identifier
    private String time;
    private double latitude;
    private double longitude;

    // Attribute
    private String chipId;
    private double temperature;
    private double voltage;
    private double pressure;
    private Integer t_patch0;
    private Integer t_patch1;
    private Integer t_period;
    private double accz;
    private Integer t_spl_period;
    private Integer t_verified;
}
