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
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CSVFormat_21_4Test {

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_defaultInstance() {
        CSVFormat format = CSVFormat.DEFAULT;
        String recordSeparator = format.getRecordSeparator();
        assertEquals("\r\n", recordSeparator);
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_customRecordSeparatorString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("XYZ");
        // Using reflection to access private final field 'recordSeparator'
        Field field = CSVFormat.class.getDeclaredField("recordSeparator");
        field.setAccessible(true);
        String recordSeparatorValue = (String) field.get(format);
        assertEquals("XYZ", recordSeparatorValue);
        assertEquals("XYZ", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_customRecordSeparatorChar() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator('Z');
        Field field = CSVFormat.class.getDeclaredField("recordSeparator");
        field.setAccessible(true);
        String recordSeparatorValue = (String) field.get(format);
        assertEquals("Z", recordSeparatorValue);
        assertEquals("Z", format.getRecordSeparator());
    }

    @Test
    @Timeout(8000)
    void testGetRecordSeparator_nullRecordSeparator() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator((String) null);
        Field field = CSVFormat.class.getDeclaredField("recordSeparator");
        field.setAccessible(true);
        String recordSeparatorValue = (String) field.get(format);
        assertNull(recordSeparatorValue);
        assertNull(format.getRecordSeparator());
    }
}