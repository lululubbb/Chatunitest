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

public class CSVFormat_6_1Test {

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
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNull(result, "Result should be null when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result, "Result should not be null for empty input array");
        assertEquals(0, result.length, "Result array length should be zero");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullValues() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] { "abc", 123, 45.6, true, 'Z' };
        String[] expected = new String[] { "abc", "123", "45.6", "true", "Z" };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertArrayEquals(expected, result, "Converted string array does not match expected values");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_MixedNullAndNonNull() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] { null, "test", null, 42 };
        String[] expected = new String[] { null, "test", null, "42" };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertArrayEquals(expected, result, "Converted string array does not handle null values correctly");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ArrayWithNullOnly() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] { null, null, null };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result, "Result should not be null for input array with nulls");
        assertEquals(3, result.length, "Result array length should match input length");
        for (String s : result) {
            assertNull(s, "Each element should be null");
        }
    }
}