package com.udp.server.spring;

import com.udp.server.service.UserService;
import io.vertx.core.AbstractVerticle;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public final class OnApplicationStartup {

    private final UserService userService;

    @EventListener
    public void execute(final ContextRefreshedEvent event) {
        this.userService.removeDisconnectTimeForUsers();
    }
}
