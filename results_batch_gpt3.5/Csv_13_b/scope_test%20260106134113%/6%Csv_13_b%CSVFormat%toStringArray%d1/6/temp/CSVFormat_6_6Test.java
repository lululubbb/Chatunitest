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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_6_6Test {

    private CSVFormat csvFormat;
    private Method toStringArrayMethod;

    @BeforeEach
    void setUp() throws Exception {
        // Using DEFAULT instance for testing
        csvFormat = CSVFormat.DEFAULT;
        // Access private method toStringArray with correct parameter type
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_NullInput() throws Exception {
        // When invoking a method with varargs or array as parameter via reflection,
        // pass new Object[] { null } to represent null array argument correctly
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { null });
        assertNull(result, "Expected null when input is null");
    }

    @Test
    @Timeout(8000)
    void testToStringArray_EmptyArray() throws Exception {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.length, "Result length should be zero");
    }

    @Test
    @Timeout(8000)
    void testToStringArray_ArrayWithNulls() throws Exception {
        Object[] input = new Object[]{null, "test", null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertNull(result[0], "First element should be null");
        assertEquals("test", result[1], "Second element should be 'test'");
        assertNull(result[2], "Third element should be null");
    }

    @Test
    @Timeout(8000)
    void testToStringArray_ArrayWithVariousObjects() throws Exception {
        Object[] input = new Object[]{123, 45.67, true, 'c', new StringBuilder("sb")};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("123", result[0]);
        assertEquals("45.67", result[1]);
        assertEquals("true", result[2]);
        assertEquals("c", result[3]);
        assertEquals("sb", result[4]);
    }
}