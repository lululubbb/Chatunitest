package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Constants.BACKSLASH;
import static org.apache.commons.csv.Constants.COMMA;
import static org.apache.commons.csv.Constants.COMMENT;
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
import java.nio.file.Path;
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

public class CSVFormat_40_5Test {

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
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullValues() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] { "abc", 123, 45.6, true, 'z' };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("abc", result[0]);
        assertEquals("123", result[1]);
        assertEquals("45.6", result[2]);
        assertEquals("true", result[3]);
        assertEquals("z", result[4]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_SomeNullValues() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] { null, "test", null, 42 };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertNull(result[0]);
        assertEquals("test", result[1]);
        assertNull(result[2]);
        assertEquals("42", result[3]);
    }
}