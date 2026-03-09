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

class CSVFormat_13_6Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultIsNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() throws Exception {
        String nullStr = "NULL_VALUE";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());

        // additionally verify via reflection that the private field is set correctly
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertEquals(nullStr, nullStringField.get(format));
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringEmpty() throws Exception {
        String nullStr = "";
        CSVFormat format = CSVFormat.DEFAULT.withNullString(nullStr);
        assertEquals(nullStr, format.getNullString());

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertEquals(nullStr, nullStringField.get(format));
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        assertNull(format.getNullString());

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        assertNull(nullStringField.get(format));
    }
}