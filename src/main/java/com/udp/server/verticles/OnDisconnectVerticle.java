package com.udp.server.verticles;

import com.udp.server.VertexQualifier;
import com.udp.server.verticles.enums.Addresses;
import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@VertexQualifier(event = VertxType.DISCONNECT)
@Slf4j
public final class OnDisconnectVerticle extends AbstractVerticle{

    @Override
    public void start() throws Exception {
        final EventBus eventBus = this.vertx.eventBus();
         eventBus.<String>consumer(Addresses.DISCONNECT.asString(), handler->{
             final String body = handler.body();
            });
        
    }
    
}
