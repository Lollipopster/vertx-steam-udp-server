package com.udp.server.verticles.enums;

/**
 *
 * @author strogiyotec
 */
public enum Addresses {
    DISCONNECT("disconnect"),
    CONNECT("connect"),
    END_MATCH("end"),
    BEGIN_MATCH("begin");
    private final String address;

    private Addresses(final String address) {
        this.address = address;
    }

    public String asString() {
        return this.address;
    }

}
