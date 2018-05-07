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

import java.util.Date;

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
                log.info("Last disconnect time [{}]",user.getDisconnectDate());
                final Period period = this.dateService.periodBetwean(user.getDisconnectDate(),now);
                final int disconnectDuration = period.getMinutes();
                if (disconnectDuration >= UserService.DURATION_BEFORE_BAN) {
                    user.setDisconnectDuration(0);
                    user.setDisconnectDate(null);
                    user.setAttempts(1);
                    user.setLastTry(new Date(System.currentTimeMillis() + (2 * ONE_MINUTE_IN_MILLIS * 60)));
                    log.warn("User {} will be banned by UDP Duration : [{}]", steamId, disconnectDuration);
                } else {
                    final int updatedDiscTime = user.getDisconnectDuration() + disconnectDuration;
                    user.setDisconnectDuration(updatedDiscTime);
                    log.warn("Update user {} disconnect time to {}", steamId, updatedDiscTime);
                }
                this.userRepository.save(user);
            }
        }
        return user;
    }

    @Override
    public Users onDisconnectAction(final String steamId) {
        final Users user = this.userRepository.findBySteamId(steamId.replaceFirst("STEAM_1", "STEAM_0"));
        if (user != null && user.isInMatch()) {
            final Date now = new Date();
            user.setDisconnectDate(now);
            this.userRepository.save(user);
            log.warn("User with steamId {} is disconnected Time : [{}]", steamId,now.toString());
        }
        return user;
    }

}
