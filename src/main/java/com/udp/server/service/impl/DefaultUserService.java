package com.udp.server.service.impl;


import com.udp.server.models.Users;
import com.udp.server.repository.UserRepository;
import com.udp.server.service.DateService;
import com.udp.server.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
@AllArgsConstructor
@Slf4j
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final DateService dateService;

    private static final long ONE_MINUTE_IN_MILLIS = 60000;

    @Override
    public Users onConnectAction(final String steamId) {
        final Date now = new Date();
        log.info("User with steamId {} connected {}", steamId,now);
        final Users user = this.userRepository.findBySteamId(steamId.replaceFirst("STEAM_1", "STEAM_0"));
        if (user != null) {
            user.setInMatch(true);
            if (user.getDisconnectDate() != null) {
                final Period period = this.dateService.periodBetwean(user.getDisconnectDate(),now);
                final int disconnectDuration = period.getMinutes();
                if (disconnectDuration >= UserService.DURATION_BEFORE_BAN) {
                    this.banUser(user,disconnectDuration);
                } else {
                    this.updateBanTime(user,disconnectDuration);
                }

            }
        }
        return user;
    }

    @Override
    public Users onDisconnectAction(final String steamId) {
        final Users user = this.userRepository.findBySteamId(steamId.replaceFirst("STEAM_1", "STEAM_0"));
        if (user != null && user.isInMatch()) {
            this.logDisconnectInDb(user);
        }
        return user;
    }

    private void banUser(final Users user,final int disconnectDuration){
        user.setDisconnectDuration(0);
        user.setDisconnectDate(null);
        user.setAttempts(1);
        user.setLastTry(new Date(System.currentTimeMillis() + (2 * ONE_MINUTE_IN_MILLIS * 60)));
        log.warn("User {} will be banned by UDP Duration : [{}]", user.getSteamId(), disconnectDuration);
        this.userRepository.save(user);
    }

    private void updateBanTime(final Users user,final int disconnectDuration){
        final int updatedDiscTime = user.getDisconnectDuration() + disconnectDuration;
        user.setDisconnectDuration(updatedDiscTime);
        user.setDisconnectDate(null);
        log.info("Update user {} disconnect time to {}", user.getSteamId(), updatedDiscTime);
        this.userRepository.save(user);
    }

    private void logDisconnectInDb(final Users user){
        final Date now = new Date();
        user.setDisconnectDate(now);
        this.userRepository.save(user);
        log.warn("User with steamId {} is disconnected", user.getSteamId());
    }

    @Override
    public void removeDisconnectTimeForUsers() {
        this.userRepository.removeDisconnectTime();
    }

}
