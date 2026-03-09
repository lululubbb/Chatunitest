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

class CSVFormat_22_6Test {

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNull() throws Exception {
        // Create a new CSVFormat instance with nullString == null using withNullString(null)
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        boolean result = format.isNullStringSet();

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsNullStringSet_whenNullStringIsNotNull() throws Exception {
        // Create a CSVFormat instance with a non-null nullString
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Use reflection to confirm the private final field nullString is set
        Field field = CSVFormat.class.getDeclaredField("nullString");
        field.setAccessible(true);
        Object nullStringValue = field.get(format);
        assertEquals("NULL", nullStringValue);

        boolean result = format.isNullStringSet();

        assertTrue(result);
    }

}