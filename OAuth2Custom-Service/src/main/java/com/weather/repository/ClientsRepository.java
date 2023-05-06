package com.weather.repository;

import com.weather.entity.Clients;

public interface ClientsRepository {
    Clients findClientByClientID(String clientID);
    Clients findClientByID(Integer id);
}
