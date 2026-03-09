package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
import static org.apache.commons.csv.Constants.EMPTY;
import static org.apache.commons.csv.Constants.CR;
import static org.apache.commons.csv.Constants.CRLF;
import static org.apache.commons.csv.Constants.DOUBLE_QUOTE_CHAR;
import static org.apache.commons.csv.Constants.LF;
import static org.apache.commons.csv.Constants.PIPE;
import static org.apache.commons.csv.Constants.SP;
import static org.apache.commons.csv.Constants.TAB;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Serializable;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
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

public class CSVFormat_42_4Test {

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
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNull(result, "Result should be null when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result, "Result should not be null for empty input array");
        assertEquals(0, result.length, "Result array length should be 0");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullObjects() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {1, 2.5, "test", Boolean.TRUE};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result, "Result should not be null");
        assertEquals(input.length, result.length, "Result array length should match input length");
        assertEquals("1", result[0]);
        assertEquals("2.5", result[1]);
        assertEquals("test", result[2]);
        assertEquals("true", result[3]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ArrayWithNullElements() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {"a", null, 123, null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[]{input});
        assertNotNull(result, "Result should not be null");
        assertEquals(input.length, result.length, "Result array length should match input length");
        assertEquals("a", result[0]);
        assertNull(result[1], "Element corresponding to null input should be null");
        assertEquals("123", result[2]);
        assertNull(result[3], "Element corresponding to null input should be null");
    }
}