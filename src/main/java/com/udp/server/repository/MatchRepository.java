package com.udp.server.repository;

import com.udp.server.models.CsMatch;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author strogiyotec
 */
public interface MatchRepository extends CrudRepository<CsMatch, Integer> {

    List<CsMatch> findByMatchIdOrderByCreationTimeDesc(int id);

    List<CsMatch> findByMatchIdGreaterThanOrderByMatchIdDesc(int matchId);

    List<CsMatch> findByMatchIdInOrOrderByMatchIdDesc(List<Integer> matches);
}
