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

class CSVFormat_6_5Test {

    private CSVFormat csvFormat;
    private Method toStringArrayMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        csvFormat = CSVFormat.DEFAULT;
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_NullInput() throws InvocationTargetException, IllegalAccessException {
        Object[] input = null;
        Object result = toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        Object result = toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(0, strings.length);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_AllNonNullObjects() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {1, "two", 3.0, true};
        Object result = toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(input.length, strings.length);
        assertEquals("1", strings[0]);
        assertEquals("two", strings[1]);
        assertEquals("3.0", strings[2]);
        assertEquals("true", strings[3]);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_WithNullElements() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {"a", null, 5};
        Object result = toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(input.length, strings.length);
        assertEquals("a", strings[0]);
        assertNull(strings[1]);
        assertEquals("5", strings[2]);
    }
}