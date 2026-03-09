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

class CSVFormat_17_2Test {

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Default() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_RFC4180() {
        CSVFormat format = CSVFormat.RFC4180;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_EXCEL() {
        CSVFormat format = CSVFormat.EXCEL;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_TDF() {
        CSVFormat format = CSVFormat.TDF;
        assertEquals("\r\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_MYSQL() {
        CSVFormat format = CSVFormat.MYSQL;
        assertEquals("\n", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomString() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat custom = base.withRecordSeparator("XYZ");
        assertEquals("XYZ", custom.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_CustomChar() {
        CSVFormat base = CSVFormat.DEFAULT;
        CSVFormat custom = base.withRecordSeparator('A');
        assertEquals("A", custom.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_Null() throws Exception {
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(
                char.class, Character.class,
                QuoteMode.class, Character.class, Character.class,
                boolean.class, boolean.class, String.class, String.class, String[].class,
                boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat format = constructor.newInstance(
                ',', null, null, null, null,
                false, false, null, null, null,
                false, true);
        assertNull(format.getRecordSeparator());
    }
}