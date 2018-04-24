package com.udp.server.verticles;

import com.udp.server.verticles.enums.Addresses;
import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.datagram.DatagramSocketOptions;
import io.vertx.core.eventbus.EventBus;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.udp.server.VertexQualifier;

/**
 * This class is the udp server that listen messages in localhost
 * Filter these messages then write message to address accoring to content of message 
 * @author strogiyotec
 */
@Component
@VertexQualifier(event = VertxType.MAIN)
@Slf4j
public final class UdpServerVerticle extends AbstractVerticle {
    
    
    @Override
    public void start() throws Exception {
        final DatagramSocket socket = this.vertx.createDatagramSocket(new DatagramSocketOptions());
        socket.listen(9000, "0.0.0.0", asyncRes -> {
            if (asyncRes.succeeded()) {
                socket.handler(packet -> {
                    final byte[] bytes = packet.data().getBytes(0, packet.data().length());
                    final String body = this.body(bytes);
                    this.chooseAddress(body);
                });
            } else {
               log.error("Can't get message",asyncRes.cause());
            }
        });
    }
    
    /**
     * 
     * @param body message from logs
     * choose address to write message
     */
    private void chooseAddress(final String body){
        if (this.diconnect(body)) {
            this.sendToEventBus(body, Addresses.DISCONNECT);
        } else if (this.connect(body)) {
            this.sendToEventBus(body, Addresses.CONNECT);
        } else if (this.gameBegin(body)) {
            this.sendToEventBus(body, Addresses.BEGIN_MATCH);
        } else if (this.gameOver(body)) {
            this.sendToEventBus(body, Addresses.END_MATCH);
        } 
    }
    
    
    /**
     * 
     * @param message to write 
     * @param address where message will be written
     */
    private void sendToEventBus(final String message,final Addresses address){
        log.info("Message {} will be transfered to addres",message,address.asString());
        final EventBus eventBus = this.vertx.eventBus();
        eventBus.send(address.asString(), message);
    }

    @SneakyThrows
    private String body(final byte[] bytes) {
       return new String(bytes, "UTF-8");
    }
    
    
    private boolean diconnect(final String body) {
        return body.contains("(reason \"Disconnect\")") || (body.contains("disconnected") && body.contains("timed out"));
    }

    private boolean connect(final String body) {
        return body.contains("entered the game");
    }

    private boolean gameOver(final String body) {
        return body.contains("map_end");
    }

    private boolean gameBegin(final String body) {
        return body.contains("map_veto");
    }
}