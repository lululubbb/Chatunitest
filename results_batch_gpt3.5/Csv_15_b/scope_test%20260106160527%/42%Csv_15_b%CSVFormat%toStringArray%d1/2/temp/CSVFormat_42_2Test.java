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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_42_2Test {

    private CSVFormat csvFormat;

    @BeforeEach
    public void setUp() {
        csvFormat = CSVFormat.DEFAULT;
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_nullInput() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        // Pass null wrapped in Object[] to invoke varargs method correctly
        String[] result = (String[]) method.invoke(csvFormat, new Object[] { null });
        assertNull(result, "Expected null when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_emptyArray() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        Object[] input = new Object[0];
        String[] result = (String[]) method.invoke(csvFormat, new Object[] { input });
        assertNotNull(result, "Result should not be null for empty array");
        assertEquals(0, result.length, "Result length should be zero for empty array");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_arrayWithNulls() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        Object[] input = new Object[] { "a", null, 123, null };
        String[] result = (String[]) method.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("a", result[0]);
        assertNull(result[1]);
        assertEquals("123", result[2]);
        assertNull(result[3]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_arrayWithVariousObjects() throws Exception {
        Method method = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        method.setAccessible(true);
        Object[] input = new Object[] { 1, 2.5, true, 'c', new StringBuilder("test") };
        String[] result = (String[]) method.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("1", result[0]);
        assertEquals("2.5", result[1]);
        assertEquals("true", result[2]);
        assertEquals("c", result[3]);
        assertEquals("test", result[4]);
    }
}