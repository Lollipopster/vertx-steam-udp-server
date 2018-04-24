package com.udp.server.service;

import com.udp.server.models.Users;

public interface UserService {

    Users onConnectAction(String steamId);

}
