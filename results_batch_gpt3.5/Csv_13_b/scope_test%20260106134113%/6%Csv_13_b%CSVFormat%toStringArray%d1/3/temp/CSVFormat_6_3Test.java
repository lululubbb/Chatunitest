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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_6_3Test {

    private CSVFormat csvFormat;
    private Method toStringArrayMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Use the DEFAULT instance for testing
        csvFormat = CSVFormat.DEFAULT;
        // Access private method toStringArray(Object[]) via reflection
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_NullInput() throws InvocationTargetException, IllegalAccessException {
        // values == null should return null
        Object[] input = null;
        // When invoking a method with a single Object[] parameter via reflection,
        // pass the argument directly (without wrapping in another Object[])
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNull(result, "Expected null output when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        // Empty array input should return empty String array
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.length, "Result length should be 0");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ArrayWithNulls() throws InvocationTargetException, IllegalAccessException {
        // Array containing null values should map null to null
        Object[] input = new Object[]{null, "abc", null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertEquals("abc", result[1]);
        assertNull(result[2]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ArrayWithVariousObjects() throws InvocationTargetException, IllegalAccessException {
        // Array with various object types should call toString on each non-null element
        Object[] input = new Object[]{123, 45.67, true, 'x', new StringBuilder("sb")};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("123", result[0]);
        assertEquals("45.67", result[1]);
        assertEquals("true", result[2]);
        assertEquals("x", result[3]);
        assertEquals("sb", result[4]);
    }

}