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

class CSVFormat_9_2Test {

    @Test
    @Timeout(8000)
    void testGetEscapeWhenEscapeIsNull() throws Exception {
        // Use reflection to create CSVFormat instance with escape = null
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, Quote.class, Character.class,
                Character.class, boolean.class, boolean.class, String.class,
                String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',', null, null, null,
                null, false, true, "\r\n",
                null, null, false);
        assertNull(format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWhenEscapeIsSet() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('\\');
        assertEquals(Character.valueOf('\\'), format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWithDifferentEscapeChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withEscape('"');
        assertEquals(Character.valueOf('"'), format.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWithDefaultFormat() {
        assertNull(CSVFormat.DEFAULT.getEscape());
    }

    @Test
    @Timeout(8000)
    void testGetEscapeWithMYSQLFormat() {
        assertEquals(Character.valueOf('\\'), CSVFormat.MYSQL.getEscape());
    }
}