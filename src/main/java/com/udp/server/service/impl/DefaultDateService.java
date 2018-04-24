package com.udp.server.service.impl;

import com.udp.server.service.DateService;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public final class DefaultDateService implements DateService{

    @Override
    public Period periodBetwean(final Date dateFrom) {
        final Date to = new Date();
        return this.periodBetwean(dateFrom,to);
    }

    @Override
    public Period periodBetwean(final Date from, final Date to) {
        final Interval interval = new Interval(from.getTime(),to.getTime());
        return interval.toPeriod();
    }
}
