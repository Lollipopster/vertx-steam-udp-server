package com.udp.server.parser;

import com.udp.server.ParsedLogBody;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@Primary
@ParserQualifier(type = ParserQualifier.ParserType.GameEvent)
@Slf4j
public final class GameEventAction implements ParsedLogBody{

    @Override
    public JsonObject get(final String body) {
        if(body.contains("BOT")){
            return null;
        }
        final String startFromSteamId = body.substring(body.indexOf("STEAM"),body.length());
        final String onlySteamId = startFromSteamId.substring(0,startFromSteamId.indexOf(">"));
        return new JsonObject().put("steamId",onlySteamId);
    }
    
}
