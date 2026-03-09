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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_6_5Test {

    @Test
    @Timeout(8000)
    public void testToStringArray_nullInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object result = toStringArray.invoke(csvFormat, (Object) null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_emptyArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[0];
        Object result = toStringArray.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(0, strings.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_withNullElements() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[] { "a", null, 123, null };
        Object result = toStringArray.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(4, strings.length);
        assertEquals("a", strings[0]);
        assertNull(strings[1]);
        assertEquals("123", strings[2]);
        assertNull(strings[3]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_allNonNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[] { "test", 42, 3.14, true, 'c' };
        Object result = toStringArray.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(5, strings.length);
        assertEquals("test", strings[0]);
        assertEquals("42", strings[1]);
        assertEquals("3.14", strings[2]);
        assertEquals("true", strings[3]);
        assertEquals("c", strings[4]);
    }
}