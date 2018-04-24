package com.udp.server.service;

import org.joda.time.Period;

import java.util.Date;

public interface DateService {

    Period periodBetwean(Date dateFrom);

    Period periodBetwean(Date from,Date dateTo);
}
