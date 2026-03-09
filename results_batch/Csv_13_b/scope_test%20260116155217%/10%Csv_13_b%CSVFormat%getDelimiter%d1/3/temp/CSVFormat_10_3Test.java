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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Constructor;

class CSVFormat_10_3Test {

    private CSVFormat csvFormatDefault;
    private CSVFormat csvFormatTab;

    @BeforeEach
    void setUp() {
        csvFormatDefault = CSVFormat.DEFAULT;
        csvFormatTab = CSVFormat.TDF;
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Default() {
        assertEquals(',', csvFormatDefault.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_Tab() {
        assertEquals('\t', csvFormatTab.getDelimiter());
    }

    @Test
    @Timeout(8000)
    void testGetDelimiter_CustomDelimiter() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                String[].class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat customFormat = constructor.newInstance(
                ';', null, null, null, null, false, false, "\n", null, new String[0],
                new String[0], false, false, false);
        assertEquals(';', customFormat.getDelimiter());
    }
}