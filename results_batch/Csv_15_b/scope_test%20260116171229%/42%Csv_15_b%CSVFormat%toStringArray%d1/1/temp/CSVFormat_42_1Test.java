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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_42_1Test {

    @Test
    @Timeout(8000)
    public void testToStringArray_NullInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);

        Object result = method.invoke(csvFormat, (Object) null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);

        Object[] input = new Object[0];
        Object result = method.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(0, strings.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_ArrayWithNullsAndObjects() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);

        Object[] input = new Object[] { null, "test", 123, new Object() {
            @Override
            public String toString() {
                return "custom";
            }
        } };

        Object result = method.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(input.length, strings.length);
        assertNull(strings[0]);
        assertEquals("test", strings[1]);
        assertEquals("123", strings[2]);
        assertEquals("custom", strings[3]);
    }
}