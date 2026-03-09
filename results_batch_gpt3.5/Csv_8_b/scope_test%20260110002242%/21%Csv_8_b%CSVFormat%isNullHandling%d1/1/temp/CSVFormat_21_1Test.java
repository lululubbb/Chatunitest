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

class CSVFormat_21_1Test {

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNull() throws Exception {
        // Create a CSVFormat instance with nullString set to null using withNullString(null)
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        // Access private field nullString via reflection and verify it is null
        Field field = CSVFormat.class.getDeclaredField("nullString");
        field.setAccessible(true);
        Object nullStrValue = field.get(format);
        assertNull(nullStrValue);

        // Invoke isNullHandling and assert false
        assertFalse(format.isNullHandling());
    }

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNotNull() throws Exception {
        // Create a CSVFormat instance with nullString set
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Access private field nullString via reflection and verify it is set
        Field field = CSVFormat.class.getDeclaredField("nullString");
        field.setAccessible(true);
        Object nullStrValue = field.get(format);
        assertEquals("NULL", nullStrValue);

        // Invoke isNullHandling and assert true
        assertTrue(format.isNullHandling());
    }
}