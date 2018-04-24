package com.udp.server.verticles;

import com.udp.server.ParsedLogBody;
import com.udp.server.VertexQualifier;
import com.udp.server.parser.ParserQualifier;
import com.udp.server.verticles.enums.Addresses;
import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.udp.server.parser.ParserQualifier.ParserType.OnConnect;

/**
 *
 * @author strogiyotec
 */
@Component
@VertexQualifier(event = VertxType.CONNECT)
@Slf4j
public final class OnConnectVerticle extends AbstractVerticle{

    private final ParsedLogBody body;

    private final

    @Autowired
    private OnConnectVerticle(@ParserQualifier(type = OnConnect)
                              final ParsedLogBody body) {
        this.body = body;
    }

    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
           eventBus.<String>consumer(Addresses.CONNECT.asString(), handler->{
                final String body = handler.body();
                log.info(body);
                final JsonObject jsonBody = this.body.get(body);
            });    
    }

    private void userConnectAction(final String steamId){

    }

}
