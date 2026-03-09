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
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_6_2Test {

    private CSVFormat csvFormat;
    private Method toStringArrayMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_NullInput() throws InvocationTargetException, IllegalAccessException {
        Object[] input = null;
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertNull(result, "Expected null output for null input");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertNotNull(result, "Result should not be null for empty array");
        assertEquals(0, result.length, "Result array length should be zero");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullObjects() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {1, 2.5, "test", true};
        String[] expected = new String[] {"1", "2.5", "test", "true"};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertArrayEquals(expected, result, "Expected string array conversion");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ContainsNulls() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {null, "abc", null, 42};
        String[] expected = new String[] {null, "abc", null, "42"};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertArrayEquals(expected, result, "Expected string array with nulls preserved");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_MixedObjects() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {new StringBuilder("sb"), new Object(), null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertEquals("sb", result[0], "StringBuilder toString conversion");
        assertNotNull(result[1], "Object toString conversion should not be null");
        assertNull(result[2], "Null should be preserved");
    }
}