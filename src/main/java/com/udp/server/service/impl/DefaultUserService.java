package com.udp.server.service.impl;


import com.udp.server.models.Users;
import com.udp.server.repository.UserRepository;
import com.udp.server.service.DateService;
import com.udp.server.service.UserService;
import lombok.AllArgsConstructor;
import org.joda.time.Period;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Transactional(rollbackFor = Exception.class)
@Service
@AllArgsConstructor
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;

    private final DateService dateService;

    private static final long ONE_MINUTE_IN_MILLIS=60000;

    @Override
    public Users onConnectAction(final String steamId) {
        final Users user = this.userRepository.findBySteamId(steamId);
        if (user != null && this.shouldBan(user)) {
            this.banUser(user);
        } else{

        }
        return null;
    }

    private void banUser(final Users user) {
            switch (user.getAttempts()) {
                case 0: {
                    final Date toBan = new Date(System.currentTimeMillis() + (2 * ONE_MINUTE_IN_MILLIS * 60));
                    user.setAttempts(1);
                    user.setLastTry(toBan);
                    this.userRepository.save(user);
                }
                case 1: {
                    final Date toBan = new Date(System.currentTimeMillis() + (ONE_MINUTE_IN_MILLIS * 1440));
                    user.setAttempts(2);
                    user.setLastTry(toBan);
                    this.userRepository.save(user);
                }
                case 2: {
                    final Date toBan = new Date(System.currentTimeMillis() + (3 * ONE_MINUTE_IN_MILLIS * 1440));
                    user.setAttempts(3);
                    user.setLastTry(toBan);
                    this.userRepository.save(user);
                }
                default: {
                    final Date toBan = new Date(System.currentTimeMillis() + (7 * ONE_MINUTE_IN_MILLIS * 1440));
                    user.setAttempts(4);
                    user.setLastTry(toBan);
                    this.userRepository.save(user);
                }
            }
    }


    private boolean shouldBan(final Users user) {
        final boolean shouldBan;
        final Date now = new Date();
        final Date from = user.getDisconnectDate();
        if (from != null && user.getDisconnectDate().compareTo(now) < 0) {
            final Period period = this.dateService.periodBetwean(from, now);
            shouldBan = period.getMinutes() >= 5;
        } else {
            shouldBan = false;
        }
        return shouldBan;
    }


}
