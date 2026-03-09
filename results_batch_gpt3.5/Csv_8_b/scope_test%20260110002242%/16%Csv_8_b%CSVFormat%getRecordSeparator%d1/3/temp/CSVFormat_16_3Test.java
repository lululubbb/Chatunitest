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

class CSVFormat_16_3Test {

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Excel() {
        CSVFormat format = CSVFormat.EXCEL;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_TDF() {
        CSVFormat format = CSVFormat.TDF;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_MySQL() {
        CSVFormat format = CSVFormat.MYSQL;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomString() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n\r");
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n\r", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomChar() {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator('\n');
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Null() throws Exception {
        Class<?> quoteClass = null;
        for (Class<?> innerClass : CSVFormat.class.getDeclaredClasses()) {
            if ("Quote".equals(innerClass.getSimpleName())) {
                quoteClass = innerClass;
                break;
            }
        }
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class, quoteClass, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',', null, null, null, null,
                false, true, null, null, null, false);
        String recordSeparator = format.getRecordSeparator();
        assertNull(recordSeparator);
    }
}