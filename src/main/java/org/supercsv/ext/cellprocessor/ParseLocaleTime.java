/*
 * ParseLocaleTime.java
 * created in 2013/03/06
 *
 * (C) Copyright 2003-2013 GreenDay Project. All rights reserved.
 */
package org.supercsv.ext.cellprocessor;

import java.sql.Time;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.supercsv.cellprocessor.ift.DateCellProcessor;


/**
 * @author T.TSUCHIE
 *
 */
public class ParseLocaleTime extends ParseLocaleDate {
    
    public ParseLocaleTime(final String format) {
        this(format, true, null, null);
    }
    
    public ParseLocaleTime(final String format, final DateCellProcessor next) {
        this(format, true, null, null, next);
    }
    
    public ParseLocaleTime(final String pattern, final boolean lenient) {
        this(pattern, lenient, Locale.getDefault(), null);
    }
    
    public ParseLocaleTime(final String pattern, final boolean lenient, final DateCellProcessor next) {
        this(pattern, lenient, Locale.getDefault(), null, next);
    }
    
    public ParseLocaleTime(final String format, final boolean lenient, final Locale locale, final TimeZone timeZone) {
        super(format, lenient, locale, timeZone);
    }
    
    public ParseLocaleTime(final String format, final boolean lenient, final Locale locale, final TimeZone timeZone, final DateCellProcessor next) {
        super(format, lenient, locale, timeZone, next);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected Object parse(final String value) throws ParseException {
        
        final Date result = formatter.get().parse(value);
        return new Time(result.getTime());
    }
}
