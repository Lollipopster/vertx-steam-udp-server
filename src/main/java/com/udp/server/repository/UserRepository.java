package com.udp.server.repository;

import com.udp.server.models.Users;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<Users,Integer>{

    Users findBySteamId(String steamId);

    List<Users> findBySteamIdIn(List<String> ids);

    @Modifying
    @Query(value = "update users set disconnect_time = null , disconnect_duration = 0, in_match = false",nativeQuery = true)
    void removeDisconnectTime();
}
