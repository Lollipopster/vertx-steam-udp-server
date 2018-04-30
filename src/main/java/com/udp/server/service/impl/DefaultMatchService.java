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
            log.info("Match with id [{}] is started",match.getMatchId());
            match.setBegin(true);
            this.matchRepository.save(match);
        } else{
            log.warn("There is no matches with id : [{}]",matchId);
        }
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

    @Override
    public int endNewMatches(final int lastMatchId) {
        final List<CsMatch> lastMatches = this.matchRepository.findByMatchIdGreaterThanOrderByMatchIdDesc(lastMatchId);
        if(!lastMatches.isEmpty()) {
            lastMatches.forEach(match->match.setBegin(false));
            this.matchRepository.save(lastMatches);
            final int matchId = lastMatches.get(0).getMatchId();
            log.info("Matches to be ended by udp: {}",lastMatches.stream()
                                                                 .map(CsMatch::getMatchId)
                                                                 .map(String::valueOf)
                                                                 .collect(Collectors.joining(",")));
            log.info("Last matchId from ban DB: {}",matchId);
            return matchId;
        }
        return 0;

    }

    private void unblockUsersFromMatch(final CsMatch match){
        if(match.getUsers() == null){
            log.warn("Match {} doesn't has users",match.getMatchId());
            return;
        }
        final List<String> steamIds = Arrays.asList(match.getUsers().split(","));
        if(!this.notEmptyAndSize10(steamIds)){
            log.warn("Match [{}] doesn't has 10 users",match.getId());
        } else{
            final List<Users> users = this.userRepository.findBySteamIdIn(steamIds);
            if(!this.notEmptyAndSize10(users)){
                log.warn("Users size in match {} is not 10, but {}",match.getId(),users.size());
            } else{
                this.logUsersToBeUnhandledFromMatch(match.getMatchId(),users);
                 this.unBlockUsers(users);
            }
        }
    }

    private void unBlockUsers(final List<Users> users){
        final List<Users> unBanedusers = users.stream()
                                              .filter(user -> user.getLastTry() == null)
                                              .collect(Collectors.toList());
        for(final Users user : unBanedusers){
            user.setDisconnectDate(null);
            user.setDisconnectDuration(0);
        }
        this.userRepository.save(unBanedusers);
    }

    private void logUsersToBeUnhandledFromMatch(final int matchId,final List<Users> users){
        final String joinedIds = users.stream()
                .map(Users::getSteamId)
                .collect(Collectors.joining(","));
        log.info("Users to be unhandled from match with id {} : {}",matchId,joinedIds);
    }

    private boolean notEmptyAndSize10(final List<? extends Object> list){
            return !list.isEmpty() && list.size() == 10;
    }
    
}
