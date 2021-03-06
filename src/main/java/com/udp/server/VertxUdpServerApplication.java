package com.udp.server;

import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "com.udp.*")
@Slf4j
@EnableTransactionManagement
@EntityScan("com.udp.server.models")
@EnableJpaRepositories("com.udp.server.repository")
public class VertxUdpServerApplication implements CommandLineRunner {
    
    @Autowired
    @VertexQualifier(event = VertxType.MAIN)
    private AbstractVerticle udp;
    
    @Autowired
    @VertexQualifier(event = VertxType.CONNECT)
    private AbstractVerticle connect;
    
    @Autowired
    @VertexQualifier(event = VertxType.DISCONNECT)
    private AbstractVerticle disconnect;
    
    @Autowired
    @VertexQualifier(event = VertxType.MATCH_BEGIN)
    private AbstractVerticle begin;
    
    @Autowired
    @VertexQualifier(event = VertxType.MATCH_END)
    private AbstractVerticle end;

    @Autowired
    @VertexQualifier(event = VertxType.JDBC)
    private AbstractVerticle jdbc;
    
    
    public static void main(String[] args) {
        SpringApplication.run(VertxUdpServerApplication.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {    
        final Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(20));
        vertx.deployVerticle(this.begin);
        vertx.deployVerticle(this.connect);
        vertx.deployVerticle(this.disconnect);
        vertx.deployVerticle(this.end);
        vertx.deployVerticle(this.udp);
        vertx.deployVerticle(this.jdbc);
        log.info("Server in port : [{}] is started",9000);
    }
}
