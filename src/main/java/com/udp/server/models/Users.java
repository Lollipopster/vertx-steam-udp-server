package com.udp.server.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
    @Temporal(TemporalType.DATE)
    private Date disconnectDate;


}
