package com.wizlab.common.domain;

import javax.persistence.*;
// import jakarta.persistence.*;

import lombok.Getter;

@Getter
@Embeddable
public class Header {

    private String controllerId;
    private String time;
    private double latitude;
    private double longitude;

    public void saveControllerId(String controllerId) {
        this.controllerId = controllerId;
    }
}
