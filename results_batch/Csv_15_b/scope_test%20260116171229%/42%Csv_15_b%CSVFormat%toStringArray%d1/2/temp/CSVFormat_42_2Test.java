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

public class CSVFormat_42_2Test {

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
    void testToStringArray_allNullElements() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] input = new Object[] {null, null, null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);

        assertNotNull(result);
        assertEquals(3, result.length);
        for (String s : result) {
            assertNull(s);
        }
    }

    @Test
    @Timeout(8000)
    void testToStringArray_mixedElements() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] input = new Object[] { "test", 123, null, true, 45.67 };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);

        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("test", result[0]);
        assertEquals("123", result[1]);
        assertNull(result[2]);
        assertEquals("true", result[3]);
        assertEquals("45.67", result[4]);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_emptyArray() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);

        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);

        assertNotNull(result);
        assertEquals(0, result.length);
    }
}