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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_6Test {

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
    public void testToStringArray_NullInput() throws IllegalAccessException, InvocationTargetException {
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { null });
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws IllegalAccessException, InvocationTargetException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullObjects() throws IllegalAccessException, InvocationTargetException {
        Object[] input = new Object[] { "abc", 123, 45.6, true };
        String[] expected = new String[] { "abc", "123", "45.6", "true" };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertArrayEquals(expected, result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_NullElements() throws IllegalAccessException, InvocationTargetException {
        Object[] input = new Object[] { null, "text", null, 5 };
        String[] expected = new String[] { null, "text", null, "5" };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { input });
        assertArrayEquals(expected, result);
    }
}