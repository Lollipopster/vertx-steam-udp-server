package com.udp.server.parser;

import com.udp.server.ParsedLogBody;
import io.vertx.core.json.JsonObject;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@Primary
@ParserQualifier(type = ParserQualifier.ParserType.GameEvent)
public final class GameEventAction implements ParsedLogBody{

    @Override
    public JsonObject get(final String body) {
        final String startFromSteamId = body.substring(body.indexOf("STEAM"),body.length());
        final String onlySteamId = startFromSteamId.substring(0,startFromSteamId.indexOf(">"));
        return new JsonObject().put("steamId",onlySteamId);
    }
    
}
