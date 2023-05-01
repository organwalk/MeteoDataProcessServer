package com.weather.service.meteorology;

import com.weather.utils.MeteorologyResult;

public interface MeteorologyService {

    MeteorologyResult getMeteorologyByHour(String station,String date,String hour,String which);
    MeteorologyResult getMeteorologyByDay(String station,String date,String which);
    MeteorologyResult getMeteorologyByDate(String station,String start_date,String end_date,String which);
    MeteorologyResult corrcoefDate(String station,String start_date,String end_date,String correlation);

    MeteorologyResult getComplexMeteorology(String station,
                                            String start_date,
                                            String end_date,
                                            String start_temperature,
                                            String end_temperature,
                                            String start_humidity,
                                            String end_humidity,
                                            String start_speed,
                                            String end_speed,
                                            String start_direction,
                                            String end_direction,
                                            String start_rain,
                                            String end_rain,
                                            String start_sunlight,
                                            String end_sunlight,
                                            String start_pm25,
                                            String end_pm25,
                                            String start_pm10,
                                            String end_pm10);

}