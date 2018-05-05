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

    private static final long ONE_MINUTE_IN_MILLIS=60000;

    /**
     * This method ban user for
     * 2 hours if disconnect time bigger than 5
     * @param steamId of user
     * @return user with this steam id
     */
    @Override
    public Users onConnectAction(final String steamId) {
        log.info("User with steamId {} connected",steamId);
        final Users user = this.userRepository.findBySteamId(steamId.replaceFirst("STEAM_1","STEAM_0"));
        if(user != null && user.getDisconnectDate() != null){
            final Period period = this.dateService.periodBetwean(user.getDisconnectDate());
            final int disconnectDuration = user.getDisconnectDuration() + period.getMinutes();
            if(disconnectDuration >=UserService.DURATION_BEFORE_BAN){
                user.setDisconnectDuration(0);
                user.setDisconnectDate(null);
                user.setAttempts(1);
                user.setLastTry(new Date(System.currentTimeMillis() + (2 * ONE_MINUTE_IN_MILLIS *60)));
                log.warn("User {} will be banned by UDP",steamId);
            } else{
                user.setDisconnectDuration(disconnectDuration);
                log.warn("User {} disconnected for {} minutes",steamId,disconnectDuration);
            }
            this.userRepository.save(user);
        }
        return user;
    }

    @Override
    public Users onDisconnectAction(final String steamId) {
        final Users user = this.userRepository.findBySteamId(steamId.replaceFirst("STEAM_1","STEAM_0"));
        if(user != null) {
            user.setDisconnectDate(new Date());
            this.userRepository.save(user);
            log.warn("User with steamId {} is disconnected",steamId);
        }
        return user;
    }

}
