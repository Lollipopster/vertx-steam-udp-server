package com.udp.server.verticles;

import com.udp.server.Json;
import com.udp.server.VertexQualifier;
import com.udp.server.service.MatchService;
import com.udp.server.verticles.enums.Addresses;
import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@VertexQualifier(event = VertxType.MATCH_BEGIN)
@AllArgsConstructor
@Slf4j
public final class OnMatchBeginVerticle extends AbstractVerticle{
    
    private final Json json;
    
    private final MatchService matchService;
    
    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
            eventBus.<String>consumer(Addresses.BEGIN_MATCH.asString(), handler->{
                final String body = handler.body();
                final JsonObject resJO = this.json.asJson(body);
                System.out.println(resJO.toString());
                final int matchId = Integer.parseInt(resJO.getString("matchid"));
                this.matchService.beginMatch(matchId);
                log.info("Match [{}] is started",matchId);
           });    
    }
}
