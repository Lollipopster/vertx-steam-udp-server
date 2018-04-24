package com.udp.server;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author strogiyotec
 */
public interface ParsedLogBody {

    JsonObject get(final String body);
    
}
