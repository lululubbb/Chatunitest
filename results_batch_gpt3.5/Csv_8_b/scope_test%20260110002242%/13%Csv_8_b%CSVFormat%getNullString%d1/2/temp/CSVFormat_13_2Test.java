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

import java.lang.reflect.Field;

class CSVFormat_13_2Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultInstance() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set nullString field to null for testing
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(format, null);
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() throws Exception {
        String nullStr = "NULL";
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set nullString field to "NULL"
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(format, nullStr);
        assertEquals(nullStr, format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;
        // Use reflection to set nullString field to null
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        nullStringField.set(format, null);
        assertNull(format.getNullString());
    }
}