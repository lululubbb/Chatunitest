package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.TAB;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

public class CSVFormat_10_4Test {

    @Test
    @Timeout(8000)
    public void testGetDelimiter_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_EXCEL() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals(',', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals('\t', format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testGetDelimiter_CustomDelimiter() throws Exception {
        Class<?> quoteModeClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("QuoteMode".equals(innerClass.getSimpleName())) {
                quoteModeClass = innerClass;
                break;
            }
        }

        // The constructor signature:
        // private CSVFormat(char delimiter, Character quoteChar, QuoteMode quoteMode,
        // Character commentStart, Character escape, boolean ignoreSurroundingSpaces,
        // boolean ignoreEmptyLines, String recordSeparator, String nullString,
        // Object[] headerComments, String[] header, boolean skipHeaderRecord,
        // boolean allowMissingColumnNames, boolean ignoreHeaderCase)

        java.lang.reflect.Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteModeClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class,
                boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);

        Object quoteModeValue = null;
        if (quoteModeClass != null) {
            Object[] enumConstants = quoteModeClass.getEnumConstants();
            if (enumConstants != null && enumConstants.length > 0) {
                for (Object constant : enumConstants) {
                    if ("NONE".equals(constant.toString())) {
                        quoteModeValue = constant;
                        break;
                    }
                }
                if (quoteModeValue == null) {
                    quoteModeValue = enumConstants[0];
                }
            }
        }

        CSVFormat format = constructor.newInstance(';', null, quoteModeValue, null, null,
                false, false, "\n", null, new Object[0], new String[0],
                false, false, false);
        assertEquals(';', format.getDelimiter());
    }
}