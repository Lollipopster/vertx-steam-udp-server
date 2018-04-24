package com.udp.server.parser;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author strogiyotec
 */
@Retention(RetentionPolicy.RUNTIME)
@Qualifier
@Target({ElementType.TYPE, ElementType.FIELD,ElementType.CONSTRUCTOR,ElementType.PARAMETER})
public @interface ParserQualifier {
    
    ParserType type();
    
    public enum ParserType {
        OnConnect, OnDisconnect
    }
}
