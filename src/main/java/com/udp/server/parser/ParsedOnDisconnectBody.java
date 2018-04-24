package com.udp.server.parser;

import com.udp.server.ParsedLogBody;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
@ParserQualifier(type = ParserQualifier.ParserType.OnDisconnect)
public final class ParsedOnDisconnectBody implements ParsedLogBody{

    @Override
    public JsonObject get(final String body) {
        return new JsonObject();
    }
    
}
