package com.udp.server.service.impl;

import com.udp.server.models.CsMatch;
import com.udp.server.repository.MatchRepository;
import com.udp.server.service.MatchService;
import java.util.ArrayList;
import java.util.List;
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
            log.info("MAtch with id [{}] is ended",match.getMatchId());
            match.setBegin(false);
            this.matchRepository.save(match);
        } else{
            log.info("There is no matches with id : [{}]",matchId);
        }
    }
    
}
