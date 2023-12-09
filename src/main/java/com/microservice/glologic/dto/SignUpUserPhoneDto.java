package com.microservice.glologic.dto;

public class SignUpUserPhoneDto {
    private Long number;
    private Integer citycode;
    private String countrycode;

    public SignUpUserPhoneDto() {
    }

    public SignUpUserPhoneDto(Long number, Integer citycode, String countrycode) {
        this.number = number;
        this.citycode = citycode;
        this.countrycode = countrycode;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public Integer getCitycode() {
        return citycode;
    }

    public void setCitycode(Integer citycode) {
        this.citycode = citycode;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }
}
