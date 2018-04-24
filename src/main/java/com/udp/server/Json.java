package com.udp.server;

import io.vertx.core.json.JsonObject;

/**
 *
 * @author strogiyotec
 */
public interface Json {
    JsonObject asJson(String body);
}
