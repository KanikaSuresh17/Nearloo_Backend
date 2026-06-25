package com.nearloo.nearloo_backend.dto;

import lombok.Data;

@Data
public class ToiletDTO {
    private String name;
    private String address;
    private String city;
    private Double latitude;
    private Double longitude;
    private Boolean isFemaleOnly;
    private Boolean isAccessible;
    private Boolean isPaid;
    private Boolean is24hr;
    private String operatingHours;
    private String status;
}