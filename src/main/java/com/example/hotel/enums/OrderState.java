package com.example.hotel.enums;


public enum OrderState {
    ReserveSuccess("1"),
    CheckInHotel("2"),
    ReserveFinished("3"),
    UserCanceled("4"),
    HotelCanceled("5");

    private String value;

    OrderState(String value) {
        this.value = value;
    }
    @Override
    public String toString() {
        return value;
    }

}