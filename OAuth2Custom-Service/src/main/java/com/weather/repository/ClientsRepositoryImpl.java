package com.weather.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.weather.entity.Clients;
import com.weather.mapper.ClientsMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ClientsRepositoryImpl implements ClientsRepository{

    private final ClientsMapper clientsMapper;
    @Override
    public Clients findClientByClientID(String clientID) {
        Clients clients = clientsMapper.selectOne(new QueryWrapper<Clients>()
                .eq("client_id",clientID)
                .select(Clients.class,info->true)
        );
        System.out.println(clients.toString());
        return clients;
    }

    @Override
    public Clients findClientByID(Integer id) {
        return clientsMapper.selectOne(new QueryWrapper<Clients>()
                .eq("id",id)
                .select(Clients.class,info->true)
        );
    }
}
