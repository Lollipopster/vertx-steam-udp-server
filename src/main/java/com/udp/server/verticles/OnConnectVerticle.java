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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.udp.server.parser.ParserQualifier.ParserType.GameEvent;
import static com.udp.server.parser.ParserQualifier.ParserType.OnConnect;

/**
 *
 * @author strogiyotec
 */
@Component
@VertexQualifier(event = VertxType.CONNECT)
@Slf4j
@AllArgsConstructor
public final class OnConnectVerticle extends AbstractVerticle{

    private final ParsedLogBody body;

    private final UserService userService;

    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
           eventBus.<String>consumer(Addresses.CONNECT.asString(), handler->{
                final String body = handler.body();
                final JsonObject jsonBody = this.body.get(body);
                this.userService.onConnectAction(jsonBody.getString("steamId"));
            });    
    }


}
