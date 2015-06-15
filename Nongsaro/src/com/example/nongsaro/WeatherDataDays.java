package com.example.nongsaro;

public class WeatherDataDays {
    String days;//날짜
    String status;//날씨

    public WeatherDataDays(String days, String status) {
        super();
        this.days = days;
        this.status = status;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}