package com.example.hotel.enums;

/**
 * @Author: chenyizong
 * @Date: 2020-02-29
 */
public enum UserType {
    Client("1"),
    VIPClient("2"),
    CopVIPClient("3"),
    HotelManager("4"),
    Manager("5"),
    MarketingManager("6");

    private String value;

    UserType(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }

}
