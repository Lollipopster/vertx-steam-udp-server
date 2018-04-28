package com.udp.server.service.impl;

import com.udp.server.models.CsMatch;
import com.udp.server.models.Users;
import com.udp.server.repository.MatchRepository;
import com.udp.server.repository.UserRepository;
import com.udp.server.service.MatchService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author strogiyotec
 */
@Service
@Transactional(rollbackFor = Exception.class)
@AllArgsConstructor
@Slf4j
public class DefaultMatchService implements MatchService{
    
    private final MatchRepository matchRepository;

    private final UserRepository userRepository;
    
    @Override
    public void beginMatch(final int matchId) {
        final List<CsMatch> matches= this.matchRepository.findByMatchIdOrderByCreationTimeDesc(matchId);
        if(!matches.isEmpty()) {
            final CsMatch match = matches.get(0);
            log.info("MAtch with id [{}] is started",match.getMatchId());
            match.setBegin(true);
            this.matchRepository.save(match);
        } else{
            log.info("There is no matches with id : [{}]",matchId);
        }
    }

    @Override
    public List<CsMatch> matches() {
        final List<CsMatch> matches = new ArrayList<>();
        this.matchRepository.findAll().forEach(matches::add);
        return matches;
    }

    @Override
    public void endMatch(final int matchId) {
       final List<CsMatch> matches= this.matchRepository.findByMatchIdOrderByCreationTimeDesc(matchId);
        if(!matches.isEmpty()) {
            final CsMatch match = matches.get(0);
            log.info("Match with id [{}] is ended by udp",match.getMatchId());
            match.setBegin(false);
            this.matchRepository.save(match);
            this.unblockUsersFromMatch(match);
        } else{
            log.info("There is no matches with id : [{}]",matchId);
        }
    }

    private void unblockUsersFromMatch(final CsMatch match){
        final List<String> steamIds = Arrays.asList(match.getUsers().split(","));
        if(!this.notEmptyAndSize10(steamIds)){
            log.warn("Match [{}] doesn't has users",match.getId());
        } else{
            final List<Users> users = this.userRepository.findBySteamIdIn(steamIds);
            if(!this.notEmptyAndSize10(users)){
                log.warn("Users size in match {} is not 10, but {}",match.getId(),users.size());
            } else{
                 this.unBlockUsers(users);
            }
        }
    }

    private void unBlockUsers(final List<Users> users){
        final List<Users> unBanedusers = users.stream()
                                              .filter(user -> user.getLastTry() == null)
                                              .collect(Collectors.toList());
        this.logUsersToBeUnhandledFromMatch(users);
        for(final Users user : unBanedusers){
            user.setDisconnectDate(null);
            user.setDisconnectDuration(0);
            log.info("With steamId {}",user.getSteamId());
        }
        this.userRepository.save(unBanedusers);
    }

    private void logUsersToBeUnhandledFromMatch(final List<Users> users){
        final String joinedIds = users.stream()
                .map(Users::getSteamId)
                .collect(Collectors.joining(","));
        log.info("Users to be unhandled from match: {}",joinedIds);
    }

    private boolean notEmptyAndSize10(final List<? extends Object> list){
            return !list.isEmpty();
    }
    
}
