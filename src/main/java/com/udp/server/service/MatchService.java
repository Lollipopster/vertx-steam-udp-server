package com.udp.server.service;

import com.udp.server.models.CsMatch;
import java.util.List;

/**
 *
 * @author strogiyotec
 */
public interface MatchService {
    void beginMatch(final int matchId);

    void endMatch(int matchId);

    int endNewMatches(List<Integer> endedMatches);
}
