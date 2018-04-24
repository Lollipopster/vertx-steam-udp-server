package com.udp.server.json;

import com.udp.server.Json;
import io.vertx.core.json.JsonObject;
import org.springframework.stereotype.Component;

/**
 *
 * @author strogiyotec
 */
@Component
public final class JsonFromString implements Json{
    
    @Override
    public JsonObject asJson(final String body) {
        if (!body.contains("{") || !body.contains("}")) {
            throw new IllegalArgumentException(String.format("String [%s] is not json", body));
        }
        return new JsonObject(body.substring(body.indexOf("{"), body.lastIndexOf("}")+1));
    }
}
