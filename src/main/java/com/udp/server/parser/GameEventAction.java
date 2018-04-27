package com.udp.server.parser;

import com.udp.server.ParsedLogBody;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@ParserQualifier(type = ParserQualifier.ParserType.OnConnect)
public final class ParsedOnConnectBody implements ParsedLogBody{

    @Override
    public JsonObject get(final String body) {
        final String startFromSteamId = body.substring(body.indexOf("STEAM"),body.length());
        final String onlySteamId = startFromSteamId.substring(0,startFromSteamId.indexOf(">"));
        return new JsonObject().put("steamId",onlySteamId);
    }
    
}
