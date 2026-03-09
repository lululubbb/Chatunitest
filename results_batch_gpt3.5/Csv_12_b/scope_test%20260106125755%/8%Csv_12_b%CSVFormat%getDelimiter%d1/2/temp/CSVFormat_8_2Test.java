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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;
import org.junit.jupiter.api.Test;

class CSVFormat_8_2Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultConstructor() throws Exception {
        // Using reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        QuoteMode quoteMode = null; // QuoteMode is an enum, passing null
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] {"a", "b"};
        boolean skipHeaderRecord = true;
        boolean allowMissingColumnNames = true;

        CSVFormat csvFormat = constructor.newInstance(delimiter, quoteChar, quoteMode, commentStart, escape,
                ignoreSurroundingSpaces, ignoreEmptyLines, recordSeparator, nullString, header,
                skipHeaderRecord, allowMissingColumnNames);

        assertEquals(delimiter, csvFormat.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_StaticConstants() {
        assertEquals(',', CSVFormat.DEFAULT.getDelimiter());
        assertEquals(',', CSVFormat.RFC4180.getDelimiter());
        assertEquals(',', CSVFormat.EXCEL.getDelimiter());
        assertEquals('\t', CSVFormat.TDF.getDelimiter());
        assertEquals('\t', CSVFormat.MYSQL.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_WithDelimiterMethod() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat modified = base.withDelimiter('|');
        assertEquals('|', modified.getDelimiter());
        // Original unchanged
        assertEquals(',', base.getDelimiter());
    }
}