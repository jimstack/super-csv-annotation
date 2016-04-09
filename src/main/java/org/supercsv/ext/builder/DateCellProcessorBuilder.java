/*
 * DateCellProcessorBuilder.java
 * created in 2013/03/05
 *
 * (C) Copyright 2003-2013 GreenDay Project. All rights reserved.
 */
package org.supercsv.ext.builder;

import java.lang.annotation.Annotation;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.cellprocessor.ift.DateCellProcessor;
import org.supercsv.cellprocessor.ift.StringCellProcessor;
import org.supercsv.ext.annotation.CsvDateConverter;
import org.supercsv.ext.cellprocessor.FormatLocaleDate;
import org.supercsv.ext.cellprocessor.ParseLocaleDate;
import org.supercsv.ext.cellprocessor.ParseLocaleSqlDate;
import org.supercsv.ext.cellprocessor.ParseLocaleTime;
import org.supercsv.ext.cellprocessor.ParseLocaleTimestamp;
import org.supercsv.ext.cellprocessor.constraint.DateRange;
import org.supercsv.ext.cellprocessor.constraint.FutureDate;
import org.supercsv.ext.cellprocessor.constraint.PastDate;
import org.supercsv.ext.exception.SuperCsvInvalidAnnotationException;
import org.supercsv.ext.util.Utils;


/**
 *
 *
 * @author T.TSUCHIE
 *
 */
public class DateCellProcessorBuilder extends AbstractCellProcessorBuilder<Date> {
    
    protected DateFormat createDateFormat(final String pattern, boolean lenient,
            final Locale locale, final TimeZone timeZone) {
        DateFormat value = new SimpleDateFormat(pattern, locale);
        value.setLenient(lenient);
        
        if(timeZone != null) {
            value.setTimeZone(timeZone);
        }
        
        return value;
    }
    
    protected CsvDateConverter getAnnotation(final Annotation[] annos) {
        
        if(annos == null || annos.length == 0) {
            return null;
        }
        
        for(Annotation anno : annos) {
            if(anno instanceof CsvDateConverter) {
                return (CsvDateConverter) anno;
            }
        }
        
        return null;
        
    }
    
    protected String getPattern(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return "yyyy-MM-dd";
        }
        
        return converterAnno.pattern();
    }
    
    protected boolean getLenient(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return true;
        }
        
        return converterAnno.lenient();
    }
    
    protected Locale getLocale(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return Locale.getDefault();
        }
        
        return Utils.getLocale(converterAnno.locale());
    }
    
    protected TimeZone getTimeZone(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return null;
        }
        
        if(converterAnno.timezone().isEmpty()) {
            return TimeZone.getDefault();
        }
        
        return TimeZone.getTimeZone(converterAnno.timezone());
    }
    
    protected String getMin(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return "";
        }
        
        return converterAnno.min();
    }
    
    protected String getMax(final CsvDateConverter converterAnno) {
        if(converterAnno == null) {
            return "";
        }
        
        return converterAnno.max();
    }
    
    protected CellProcessor prependRangeProcessor(final Date min, final Date max, final DateFormat formatter, final CellProcessor processor) {
        
        CellProcessor cellProcessor = processor;
        if(min != null && max != null) {
            if(cellProcessor == null) {
                cellProcessor = new DateRange<Date>(min, max).setFormatter(formatter);
            } else {
                cellProcessor = new DateRange<Date>(min, max, cellProcessor).setFormatter(formatter);
            }
        } else if(min != null) {
            if(cellProcessor == null) {
                cellProcessor = new FutureDate<Date>(min).setFormatter(formatter);
            } else {
                cellProcessor = new FutureDate<Date>(min, cellProcessor).setFormatter(formatter);
            }
        } else if(max != null) {
            if(cellProcessor == null) {
                cellProcessor = new PastDate<Date>(max).setFormatter(formatter);
            } else {
                cellProcessor = new PastDate<Date>(max, cellProcessor).setFormatter(formatter);
            }
        }
        
        return cellProcessor;
    }
    
    protected Date parseDate(final String value, final DateFormat formatter) {
        if(value.isEmpty()) {
            return null;
        }
        
        if(formatter == null) {
            return null;
        }
        
        try {
            return formatter.parse(value);
        } catch(ParseException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    @Override
    public CellProcessor buildOutputCellProcessor(final Class<Date> type, final Annotation[] annos,
            final CellProcessor processor, final boolean ignoreValidationProcessor) {
        
        final CsvDateConverter converterAnno = getAnnotation(annos);
        final String pattern = getPattern(converterAnno);
        final boolean lenient = getLenient(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final TimeZone timeZone = getTimeZone(converterAnno);
        
        final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
        
        final Date min = parseDate(getMin(converterAnno), formatter);
        final Date max = parseDate(getMax(converterAnno), formatter);
        
        CellProcessor cellProcessor = processor;
        cellProcessor = (cellProcessor == null ? 
                new FormatLocaleDate(formatter) : 
                    new FormatLocaleDate(formatter, (StringCellProcessor) cellProcessor));
        
        if(!ignoreValidationProcessor) {
            cellProcessor = prependRangeProcessor(min, max, formatter, cellProcessor);
        }
        return cellProcessor;
    }

    @Override
    public CellProcessor buildInputCellProcessor(final Class<Date> type, final Annotation[] annos,
            final CellProcessor processor) {
        
        final CsvDateConverter converterAnno = getAnnotation(annos);
        final String pattern = getPattern(converterAnno);
        final boolean lenient = getLenient(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final TimeZone timeZone = getTimeZone(converterAnno);
        
        final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
        
        final Date min = parseDate(getMin(converterAnno), formatter);
        final Date max = parseDate(getMax(converterAnno), formatter);
        
        CellProcessor cellProcessor = processor;
        cellProcessor = prependRangeProcessor(min, max, formatter, cellProcessor);
        
        cellProcessor = (cellProcessor == null ?
                new ParseLocaleDate(formatter) :
                    new ParseLocaleDate(formatter, (DateCellProcessor)cellProcessor));
        
        return cellProcessor;
        
    }
    
    public static SqlDateCellProcessorBuilder newSqlDate() {
        return new SqlDateCellProcessorBuilder();
    }
    
    public static TimestampCellProcessorBuilder newTimestamp() {
        return new TimestampCellProcessorBuilder();
    }
    
    public static TimeCellProcessorBuilder newTime() {
        return new TimeCellProcessorBuilder();
    }
    
    public static class SqlDateCellProcessorBuilder extends DateCellProcessorBuilder {
        
        @Override
        protected String getPattern(final CsvDateConverter converterAnno) {
            if(converterAnno == null) {
                return "yyyy-MM-dd";
            }
            
            return converterAnno.pattern();
        }
        
        @Override
        public CellProcessor buildInputCellProcessor(final Class<Date> type, final Annotation[] annos, 
                final CellProcessor processor) {
            
            final CsvDateConverter converterAnno = getAnnotation(annos);
            final String pattern = getPattern(converterAnno);
            final boolean lenient = getLenient(converterAnno);
            final Locale locale = getLocale(converterAnno);
            final TimeZone timeZone = getTimeZone(converterAnno);
            
            final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
            
            final java.sql.Date min = parseDate(getMin(converterAnno), formatter);
            final java.sql.Date max = parseDate(getMax(converterAnno), formatter);
            
            CellProcessor cellProcessor = processor;
            cellProcessor = prependRangeProcessor(min, max, formatter, cellProcessor);
            
            cellProcessor = (cellProcessor == null ?
                    new ParseLocaleSqlDate(formatter) :
                        new ParseLocaleSqlDate(formatter, (DateCellProcessor)cellProcessor));
            
            return cellProcessor;
            
        }
        
        @Override
        protected java.sql.Date parseDate(final String value, final DateFormat formatter) {
            Date date = super.parseDate(value, formatter);
            return date == null ? null : new java.sql.Date(date.getTime());
        }
        
        @Override
        public Date getParseValue(final Class<Date> type, final Annotation[] annos, final String defaultValue) {
            return new java.sql.Date(super.getParseValue(type, annos, defaultValue).getTime());
        }
        
    }
    
    public static class TimestampCellProcessorBuilder extends DateCellProcessorBuilder {
        
        @Override
        protected String getPattern(final CsvDateConverter converterAnno) {
            if(converterAnno == null) {
                return "yyyy-MM-dd HH:mm:ss.SSS";
            }
            
            return converterAnno.pattern();
        }
        
        @Override
        public CellProcessor buildInputCellProcessor(final Class<Date> type, final Annotation[] annos,
                final CellProcessor processor) {
            
            final CsvDateConverter converterAnno = getAnnotation(annos);
            final String pattern = getPattern(converterAnno);
            final boolean lenient = getLenient(converterAnno);
            final Locale locale = getLocale(converterAnno);
            final TimeZone timeZone = getTimeZone(converterAnno);
            
            final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
            
            final Timestamp min = parseDate(getMin(converterAnno), formatter);
            final Timestamp max = parseDate(getMax(converterAnno), formatter);
            
            CellProcessor cellProcessor = processor;
            cellProcessor = prependRangeProcessor(min, max, formatter, cellProcessor);
            
            cellProcessor = (cellProcessor == null ?
                    new ParseLocaleTimestamp(formatter) :
                        new ParseLocaleTimestamp(formatter, (DateCellProcessor)cellProcessor));
            
            return cellProcessor;
            
        }
        
        @Override
        protected Timestamp parseDate(final String value, final DateFormat formatter) {
            Date date = super.parseDate(value, formatter);
            return date == null ? null : new Timestamp(date.getTime());
        }
        
        @Override
        public Date getParseValue(final Class<Date> type, final Annotation[] annos, final String defaultValue) {
            return new Timestamp(super.getParseValue(type, annos, defaultValue).getTime());
        }
        
    }
    
    public static class TimeCellProcessorBuilder extends DateCellProcessorBuilder {
        
        @Override
        protected String getPattern(final CsvDateConverter converterAnno) {
            if(converterAnno == null) {
                return "HH:mm";
            }
            
            return converterAnno.pattern();
        }
        
        @Override
        public CellProcessor buildInputCellProcessor(final Class<Date> type, final Annotation[] annos,
                final CellProcessor processor) {
            
            final CsvDateConverter converterAnno = getAnnotation(annos);
            final String pattern = getPattern(converterAnno);
            final boolean lenient = getLenient(converterAnno);
            final Locale locale = getLocale(converterAnno);
            final TimeZone timeZone = getTimeZone(converterAnno);
            
            final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
            
            final Time min = parseDate(getMin(converterAnno), formatter);
            final Time max = parseDate(getMax(converterAnno), formatter);
            
            CellProcessor cellProcessor = processor;
            cellProcessor = prependRangeProcessor(min, max, formatter, cellProcessor);
            cellProcessor = (cellProcessor == null ?
                    new ParseLocaleTime(formatter) :
                        new ParseLocaleTime(formatter, (DateCellProcessor)cellProcessor));
            
            return cellProcessor;
            
        }
        
        @Override
        protected Time parseDate(final String value, final DateFormat formatter) {
            Date date = super.parseDate(value, formatter);
            return date == null ? null : new Time(date.getTime());
        }
        
        @Override
        public Date getParseValue(final Class<Date> type, final Annotation[] annos, final String defaultValue) {
            return new Time(super.getParseValue(type, annos, defaultValue).getTime());
        }
    }
    
    @Override
    public Date getParseValue(final Class<Date> type, final Annotation[] annos, final String defaultValue) {
        final CsvDateConverter converterAnno = getAnnotation(annos);
        final String pattern = getPattern(converterAnno);
        final boolean lenient = getLenient(converterAnno);
        final Locale locale = getLocale(converterAnno);
        final TimeZone timeZone = getTimeZone(converterAnno);
        
        final DateFormat formatter = createDateFormat(pattern, lenient, locale, timeZone);
        try {
            return formatter.parse(defaultValue);
        } catch (ParseException e) {
            throw new SuperCsvInvalidAnnotationException(
                    String.format(" default '%s' value cannot parse to Date with pattern '%s'",
                            defaultValue, pattern), e);
        }
    }
    
}
