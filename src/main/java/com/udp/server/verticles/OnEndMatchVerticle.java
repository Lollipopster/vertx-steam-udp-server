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
@VertexQualifier(event = VertxType.MATCH_END)
@AllArgsConstructor
@Slf4j
public final class OnEndMatchVerticle extends AbstractVerticle {

    private final Json json;

    private final MatchService matchService;

    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
            eventBus.<String>consumer(Addresses.END_MATCH.asString(), handler -> {
                final String body = handler.body();
                final JsonObject resJO = this.json.asJson(body);
                final int matchId = Integer.parseInt(resJO.getString("matchid"));
                this.vertx.executeBlocking(future->{
                    this.matchService.endMatch(matchId);
                    future.complete();
                },false,asyncResult -> {
                    if(asyncResult.failed()){
                        log.error("Error while endMatch",asyncResult.cause());
                    }
                });
             });
        
    }

}
