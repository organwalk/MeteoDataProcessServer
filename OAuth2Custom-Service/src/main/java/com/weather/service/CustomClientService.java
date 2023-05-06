package com.weather.service;

import com.weather.entity.Clients;
import com.weather.mapper.ClientsMapper;
import com.weather.repository.ClientsRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional//标记该类为事务处理
@AllArgsConstructor
public class CustomClientService implements RegisteredClientRepository {

    private final ClientsMapper clientsMapper;
    private final ClientsRepository clientsRepository;

    @Override
    public void save(RegisteredClient registeredClient) {
        clientsMapper.insert(Clients.from(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return Clients.from(clientsRepository.findClientByID(Integer.valueOf(id)));
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return Clients.from(clientsRepository.findClientByClientID(clientId));
    }
}
