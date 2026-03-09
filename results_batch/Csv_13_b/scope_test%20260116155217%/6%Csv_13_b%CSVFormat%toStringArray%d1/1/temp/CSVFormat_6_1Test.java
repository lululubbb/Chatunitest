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

class CSVFormat_6_1Test {

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
        // When passing varargs Object[] via reflection, wrap input directly (not new Object[]{input})
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_ArrayWithNulls() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {null, "test", null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(3, result.length);
        assertNull(result[0]);
        assertEquals("test", result[1]);
        assertNull(result[2]);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_ArrayWithVariousObjects() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {123, 45.67, true, 'c', new StringBuilder("sb")};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(5, result.length);
        assertEquals("123", result[0]);
        assertEquals("45.67", result[1]);
        assertEquals("true", result[2]);
        assertEquals("c", result[3]);
        assertEquals("sb", result[4]);
    }
}