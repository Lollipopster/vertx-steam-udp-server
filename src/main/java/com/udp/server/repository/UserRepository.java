package com.udp.server.repository;

import com.udp.server.models.Users;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users,Integer>{

    Users findBySteamId(String steamId);

    List<Users> findBySteamIdIn(List<String> ids);
}
