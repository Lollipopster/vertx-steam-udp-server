package com.udp.server.repository;

import com.udp.server.models.Users;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<Users,Integer>{

    Users findBySteamId(String steamId);
}
