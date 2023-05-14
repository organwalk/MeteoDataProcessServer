package com.weather.service.station;

import com.weather.utils.StationDateResult;

public interface StationDateService {
    StationDateResult getStationDateByStationId(String station);
    StationDateResult getCollectionYear(String station);
    StationDateResult getCollectionMonth(String station,String year);
    StationDateResult getCollectionDay(String station,String year,String month);
    StationDateResult getStationDataSum(String station,String year,String month);

}
