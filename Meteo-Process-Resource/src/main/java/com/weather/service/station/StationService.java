package com.weather.service.station;

import com.weather.utils.MeteorologyResult;
import com.weather.utils.StationDateResult;
import com.weather.utils.StationResult;

public interface StationService {
    StationResult getStationInfo(String authorization);
    StationDateResult getStationDateByStationId(String station, String authorization);
    StationDateResult getCollectionYear(String station);
    StationDateResult getCollectionMonth(String station,String year);
    StationDateResult getCollectionDay(String station,String year,String month);
    MeteorologyResult getStationDataSum(String station, String year, String month);
}
