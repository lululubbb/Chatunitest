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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.Quote;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

class CSVFormat_17_6Test {

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_DefaultFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat modifiedFormat = setSkipHeaderRecord(format, false);
        assertFalse(modifiedFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_CustomTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(true);
        CSVFormat modifiedFormat = setSkipHeaderRecord(format, true);
        assertTrue(modifiedFormat.getSkipHeaderRecord());
    }

    @Test
    @Timeout(8000)
    void testGetSkipHeaderRecord_CustomFalse() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withSkipHeaderRecord(false);
        CSVFormat modifiedFormat = setSkipHeaderRecord(format, false);
        assertFalse(modifiedFormat.getSkipHeaderRecord());
    }

    private CSVFormat setSkipHeaderRecord(CSVFormat format, boolean value) throws Exception {
        Class<CSVFormat> clazz = CSVFormat.class;

        char delimiter = getFieldValue(format, "delimiter");
        Character quoteChar = getFieldValue(format, "quoteChar");
        Quote quotePolicy = getFieldValue(format, "quotePolicy");
        Character commentStart = getFieldValue(format, "commentStart");
        Character escape = getFieldValue(format, "escape");
        boolean ignoreSurroundingSpaces = getFieldValue(format, "ignoreSurroundingSpaces");
        boolean ignoreEmptyLines = getFieldValue(format, "ignoreEmptyLines");
        String recordSeparator = getFieldValue(format, "recordSeparator");
        String nullString = getFieldValue(format, "nullString");
        String[] header = getFieldValue(format, "header");

        Constructor<CSVFormat> constructor = clazz.getDeclaredConstructor(
                char.class,
                Character.class,
                Quote.class,
                Character.class,
                Character.class,
                boolean.class,
                boolean.class,
                String.class,
                String.class,
                String[].class,
                boolean.class
        );
        constructor.setAccessible(true);

        return constructor.newInstance(
                delimiter,
                quoteChar,
                quotePolicy,
                commentStart,
                escape,
                ignoreSurroundingSpaces,
                ignoreEmptyLines,
                recordSeparator,
                nullString,
                header,
                value
        );
    }

    @SuppressWarnings("unchecked")
    private <T> T getFieldValue(CSVFormat format, String fieldName) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(format);
    }
}