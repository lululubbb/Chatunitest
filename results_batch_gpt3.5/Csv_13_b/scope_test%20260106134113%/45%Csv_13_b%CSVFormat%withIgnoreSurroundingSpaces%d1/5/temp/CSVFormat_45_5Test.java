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

class CSVFormat_45_5Test {

    @Test
    @Timeout(8000)
    void testWithIgnoreSurroundingSpaces() {
        CSVFormat format = CSVFormat.DEFAULT;
        assertFalse(format.getIgnoreSurroundingSpaces());

        CSVFormat newFormat = format.withIgnoreSurroundingSpaces();
        assertNotNull(newFormat);
        assertTrue(newFormat.getIgnoreSurroundingSpaces());

        // Original format remains unchanged (immutability)
        assertFalse(format.getIgnoreSurroundingSpaces());

        // Also test withIgnoreSurroundingSpaces(true) explicitly via reflection
        try {
            Method method = CSVFormat.class.getMethod("withIgnoreSurroundingSpaces", Boolean.TYPE);
            CSVFormat reflectedFormat = (CSVFormat) method.invoke(format, true);
            assertNotNull(reflectedFormat);
            assertTrue(reflectedFormat.getIgnoreSurroundingSpaces());
        } catch (ReflectiveOperationException e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}