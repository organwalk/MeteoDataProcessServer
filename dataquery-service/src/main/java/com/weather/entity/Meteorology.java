package com.weather.entity;

import com.baomidou.mybatisplus.extension.activerecord.Model;


public class Meteorology extends Model<Meteorology> {

    private int id;//不出现在查询结果中
    private String station;//不出现在查询结果中
    private String date;//不出现在查询结果中
    private String dateTime;//不出现在查询结果中
    private String time;
    private String temperature;
    private String humidity;
    private String speed;
    private String direction;
    private String rain;
    private String sunlight;
    private String pm25;
    private String pm10;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStation() {
        return station;
    }

    public void setStation(String station) {
        this.station = station;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getRain() {
        return rain;
    }

    public void setRain(String rain) {
        this.rain = rain;
    }

    public String getSunlight() {
        return sunlight;
    }

    public void setSunlight(String sunlight) {
        this.sunlight = sunlight;
    }

    public String getPm25() {
        return pm25;
    }

    public void setPm25(String pm25) {
        this.pm25 = pm25;
    }

    public String getPm10() {
        return pm10;
    }

    public void setPm10(String pm10) {
        this.pm10 = pm10;
    }


    @Override
    public String toString() {
        return "[" +
                "\"" + time + "\"," +
                "\"" + temperature + "\"," +
                "\"" + humidity + "\"," +
                "\"" + speed + "\"," +
                "\"" + direction + "\"," +
                "\"" + rain + "\"," +
                "\"" + sunlight + "\"," +
                "\"" + pm25 + "\"," +
                "\"" + pm10 + "\"" +
                "]";
    }


}
