package com.udp.server.parser;

import com.udp.server.ParsedLogBody;
import io.vertx.core.json.JsonObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author strogiyotec
 */
@Component
@Primary
@ParserQualifier(type = ParserQualifier.ParserType.GameEvent)
@Slf4j
public final class GameEventAction implements ParsedLogBody{
    private static final String STEAM_ID_PATTERN = "STEAM_1:1:|STEAM_1:0:\\d{7,13}";
    @Override
    public JsonObject get(final String body) {
        if(body.contains("BOT")){
            return null;
        }
        final String steamId = this.steamIdFromBody(body);
        return new JsonObject().put("steamId",steamId);
    }

    private String steamIdFromBody(final String body) {
        final Pattern pattern = Pattern.compile(GameEventAction.STEAM_ID_PATTERN);
        final Matcher matcher = pattern.matcher(body);
        if(matcher.find()){
            return matcher.group();
        } else{
            throw new IllegalArgumentException(String.format("Body %s doesn't has steam id ",body));
        }

    }

}
