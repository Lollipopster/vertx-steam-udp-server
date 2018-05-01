package com.udp.server.verticles;

import com.udp.server.ParsedLogBody;
import com.udp.server.VertexQualifier;
import com.udp.server.parser.ParserQualifier;
import com.udp.server.service.UserService;
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
@VertexQualifier(event = VertxType.DISCONNECT)
@Slf4j
@AllArgsConstructor
public final class OnDisconnectVerticle extends AbstractVerticle{

    private final ParsedLogBody parsedLogBody;

    private final UserService userService;

    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
         eventBus.<String>consumer(Addresses.DISCONNECT.asString(), handler->{
             final String body = handler.body();
             final JsonObject resJO = this.parsedLogBody.get(body);
             if(resJO != null){
                 this.vertx.executeBlocking(future->{
                     this.userService.onDisconnectAction(resJO.getString("steamId"));
                     future.complete();
                 },false,asyncResult->{
                     if(asyncResult.failed()){
                         log.error("Error while onDisconnectAction",asyncResult.cause());
                     }
                 });
             }
            });
        
    }
    
}
