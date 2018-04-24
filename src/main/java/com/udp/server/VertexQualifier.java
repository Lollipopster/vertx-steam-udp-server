package com.udp.server;

import com.udp.server.verticles.enums.VertxType;
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
@Target({ElementType.CONSTRUCTOR, ElementType.TYPE, ElementType.PARAMETER, ElementType.FIELD})
public @interface VertexQualifier {

    VertxType event();
}
