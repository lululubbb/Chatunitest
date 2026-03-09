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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class CSVFormat_13_3Test {

    @Test
    @Timeout(8000)
    void testGetNullString_DefaultConstructorNull() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertNull(format.getNullString());
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullString() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");
        assertEquals("NULL", format.getNullString());

        // Additional reflection check to ensure immutability and correct field set
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String fieldValue = (String) nullStringField.get(format);
        assertEquals("NULL", fieldValue);
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringEmpty() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString("");
        assertEquals("", format.getNullString());

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String fieldValue = (String) nullStringField.get(format);
        assertEquals("", fieldValue);
    }

    @Test
    @Timeout(8000)
    void testGetNullString_WithNullStringNull() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);
        assertNull(format.getNullString());

        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        String fieldValue = (String) nullStringField.get(format);
        assertNull(fieldValue);
    }
}