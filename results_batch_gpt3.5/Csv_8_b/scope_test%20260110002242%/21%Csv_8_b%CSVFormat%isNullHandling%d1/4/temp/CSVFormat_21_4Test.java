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

class CSVFormat_21_4Test {

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNull() throws Exception {
        // Create a new CSVFormat instance with nullString = null using withNullString method
        CSVFormat format = CSVFormat.DEFAULT.withNullString(null);

        // Use reflection to get the 'nullString' field from the instance's class (CSVFormat)
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);

        // Confirm nullString is null
        Object nullStringValue = nullStringField.get(format);
        assertNull(nullStringValue);

        assertFalse(format.isNullHandling());
    }

    @Test
    @Timeout(8000)
    void testIsNullHandlingWhenNullStringIsNotNull() throws Exception {
        // Create a CSVFormat instance with a non-null nullString using withNullString method
        CSVFormat format = CSVFormat.DEFAULT.withNullString("NULL");

        // Use reflection to confirm nullString is set
        Field nullStringField = CSVFormat.class.getDeclaredField("nullString");
        nullStringField.setAccessible(true);
        Object nullStringValue = nullStringField.get(format);
        assertNotNull(nullStringValue);

        assertTrue(format.isNullHandling());
    }
}