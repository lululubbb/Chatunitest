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
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;

class CSVFormat_8_4Test {

    @Test
    @Timeout(8000)
    void testGetDelimiter_DefaultConstructor() throws Exception {
        // Use reflection to access the private constructor
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class,
                CSVFormat.Quote.class, Character.class, Character.class, boolean.class, boolean.class,
                String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);

        char delimiter = ';';
        Character quoteChar = '"';
        CSVFormat.Quote quotePolicy = null;
        Character commentStart = '#';
        Character escape = '\\';
        boolean ignoreSurroundingSpaces = true;
        boolean ignoreEmptyLines = false;
        String recordSeparator = "\n";
        String nullString = "NULL";
        String[] header = new String[] {"A", "B"};
        boolean skipHeaderRecord = true;

        CSVFormat format = constructor.newInstance(delimiter, quoteChar, quotePolicy,
                commentStart, escape, ignoreSurroundingSpaces, ignoreEmptyLines,
                recordSeparator, nullString, header, skipHeaderRecord);

        assertEquals(delimiter, format.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_StaticDefaults() {
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
        // Original should be unchanged
        assertEquals(',', base.getDelimiter());
    }
}