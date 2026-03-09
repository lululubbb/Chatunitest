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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_47_3Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_noArg_callsWithIgnoreHeaderCaseTrue() throws Exception {
        CSVFormat format = CSVFormat.DEFAULT;

        // Use reflection to get public method withIgnoreHeaderCase(boolean)
        Method method = CSVFormat.class.getMethod("withIgnoreHeaderCase", boolean.class);

        // Invoke the focal method
        CSVFormat result = format.withIgnoreHeaderCase();

        // Invoke the method with true using reflection
        CSVFormat expected = (CSVFormat) method.invoke(format, true);

        // Check that the result equals the expected CSVFormat returned by method
        assertNotNull(result);
        assertEquals(expected, result);
        assertTrue(result.getIgnoreHeaderCase());
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_boolean_true() {
        CSVFormat format = CSVFormat.DEFAULT;
        CSVFormat changed = format.withIgnoreHeaderCase(true);
        assertNotNull(changed);
        assertTrue(changed.getIgnoreHeaderCase());
        if (!format.getIgnoreHeaderCase()) {
            assertNotEquals(format, changed);
        }
    }

    @Test
    @Timeout(8000)
    void testWithIgnoreHeaderCase_boolean_false() {
        CSVFormat format = CSVFormat.DEFAULT.withIgnoreHeaderCase(true);
        CSVFormat changed = format.withIgnoreHeaderCase(false);
        assertNotNull(changed);
        assertFalse(changed.getIgnoreHeaderCase());
        if (format.getIgnoreHeaderCase()) {
            assertNotEquals(format, changed);
        }
    }
}