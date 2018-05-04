package com.udp.server.service;

import com.udp.server.models.Users;

public interface UserService {

    public static final int DURATION_BEFORE_BAN = 5;

    Users onConnectAction(String steamId);

    Users onDisconnectAction(String steamId);
}
