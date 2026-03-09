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

public class CSVFormat_21_2Test {

    @Test
    @Timeout(8000)
    void testIsNullHandling_whenNullStringIsNull() throws Exception {
        // Create CSVFormat instance with nullString = null using withNullString method
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        assertFalse(format.isNullHandling());
    }

    @Test
    @Timeout(8000)
    void testIsNullHandling_whenNullStringIsNonNull() throws Exception {
        // Create CSVFormat instance with nullString = "NULL" using withNullString method
        CSVFormat formatWithNullString = CSVFormat.DEFAULT.withNullString("NULL");

        assertTrue(formatWithNullString.isNullHandling());

        // Also verify via reflection that nullString is set properly
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Object value = nullStringField.get(formatWithNullString);
        assertEquals("NULL", value);
    }
}