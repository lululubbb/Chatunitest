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
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CSVFormat_40_4Test {

    @Test
    @Timeout(8000)
    void testToStringArray_nullInput() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object result = toStringArrayMethod.invoke(csvFormat, (Object) null);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_emptyArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] input = new Object[0];
        Object result = toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;
        assertEquals(0, strings.length);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_withNullsAndObjects() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] input = new Object[] { null, 123, "abc", 45.6, new Object() {
            @Override
            public String toString() {
                return "customObject";
            }
        } };

        Object result = toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertTrue(result instanceof String[]);
        String[] strings = (String[]) result;

        assertEquals(input.length, strings.length);
        assertNull(strings[0]);
        assertEquals("123", strings[1]);
        assertEquals("abc", strings[2]);
        assertEquals("45.6", strings[3]);
        assertEquals("customObject", strings[4]);
    }
}