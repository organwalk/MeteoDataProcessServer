package com.weather.service.meteorology;

import com.weather.utils.MeteorologyResult;

public interface MeteorologyService {

    MeteorologyResult getMeteorologyByHour(String station,String date,String hour,String which);
    MeteorologyResult getMeteorologyByDay(String station,String date,String which);
    MeteorologyResult getMeteorologyByDate(String station,String start_date,String end_date,String which);

}
