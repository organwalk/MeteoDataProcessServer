package com.weather.service.station;

import com.weather.utils.StationDateResult;

public interface StationDateService {
    StationDateResult getStationDateByStationId(int id,String station);
}
