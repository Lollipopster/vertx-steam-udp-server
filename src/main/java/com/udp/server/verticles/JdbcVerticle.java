package com.udp.server.verticles;

import com.udp.server.CronJob;
import com.udp.server.VertexQualifier;
import com.udp.server.service.MatchService;
import com.udp.server.verticles.enums.VertxType;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.ResultSet;
import io.vertx.ext.sql.SQLConnection;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@VertexQualifier(event = VertxType.JDBC)
@Slf4j
@PropertySource("classpath:application.properties")
public final class JdbcVerticle extends AbstractVerticle implements CronJob{



    private int lastMatchId;

    private static final long FIFTEEN_MIN_IN_MILLIS = 60000 * 15;

    private final MatchService matchService;

    private JDBCClient jdbcClient;

    private final Environment environment;

    public JdbcVerticle(final MatchService matchService,final Environment environment) {
        this.matchService = matchService;
        this.environment = environment;
    }

    @Override
    public void start() throws Exception {
        this.jdbcClient = JDBCClient.createShared(this.vertx, new JsonObject()
                .put("url", this.environment.getProperty("get5match.db.url"))
                .put("driver_class", "com.mysql.jdbc.Driver")
                .put("user", this.environment.getProperty("spring.datasource.username"))
                .put("password", this.environment.getProperty("spring.datasource.password")));
        this.initLastMatchId();
        this.execute();
    }

    @Override
    public void execute() {
        this.vertx.setPeriodic(JdbcVerticle.FIFTEEN_MIN_IN_MILLIS, handler->{
            log.info("Job to end matches is started");
            this.selectEndedMatches();
        });
    }


    private void initLastMatchId(){
        this.jdbcClient.getConnection(conn->{
            if(conn.failed()){
                log.error("Can't get Vertx connection",conn.cause());
            } else{
                final SQLConnection connection = conn.result();
                connection.query("select matchid from get5_stats_matches order by matchid desc limit 1",this::selectHandler);
                connection.close();
            }
        });
    }

    private void selectEndedMatches(){
        this.jdbcClient.getConnection(conn->{
            if(conn.failed()){
                log.error("Can't get Vertx connection",conn.cause());
            } else{
                final SQLConnection connection = conn.result();
                connection.queryWithParams("select matchid from get5_stats_matches where matchid > ? and end_time is not null",new JsonArray().add(this.lastMatchId),this::endedMatches);
                connection.close();
            }
        });
    }

    private void selectHandler(final AsyncResult<ResultSet> rs) {
        if(rs.failed()){
            log.error("Can't make select statement from JdbcVerticle",rs.cause());
        } else{
            this.lastMatchId = rs.result().getResults().get(0).getInteger(0);
            log.info("On startup last matchid is : {}",this.lastMatchId);
        }
    }

    private void endedMatches(final AsyncResult<ResultSet> rs) {
        if(rs.failed()){
            log.error("Can't make select statement from JdbcVerticle",rs.cause());
        } else{
            final List<JsonArray> results = rs.result().getResults();
            final List<Integer> endedMatches = new ArrayList<>(results.size());
            for (final JsonArray result : results) {
                endedMatches.add(result.getInteger(0));
            }
            this.vertx.<Integer>executeBlocking(future -> {
                future.complete(this.matchService.endNewMatches(endedMatches));
            },false, this::updateLastMatchId);
        }
    }

    private void updateLastMatchId(final AsyncResult<Integer> result){
        if(result.failed()){
            log.error("Error while ended matches event ",result.cause());
        } else{
            final int i = result.result();
            if(i != 0){
                this.lastMatchId = i;
            }
        }
    }
}
