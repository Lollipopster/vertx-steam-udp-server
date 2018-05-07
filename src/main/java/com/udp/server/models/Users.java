package com.udp.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 *alter table users add column disconnect_time datetime;
 * @author strogiyotec
 */
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(of="steamId")
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "steam_id")
    private String steamId;

    @Column(name = "disconnect_duration",columnDefinition = "int not null default 0")
    private int disconnectDuration;

    @Column(name = "in_white_list")
    private boolean inWhiteList;

    @Column(name = "attempts")
    private int attempts;

    @Column(name = "last_try")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTry;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "steam_id_64")
    private String steamId64;

    @Column(name = "disconnect_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date disconnectDate;

    @Column(name = "in_match",nullable = false)
    private boolean inMatch;


}
