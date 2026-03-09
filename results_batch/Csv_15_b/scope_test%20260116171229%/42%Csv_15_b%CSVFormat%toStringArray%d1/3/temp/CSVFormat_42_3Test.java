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

import org.apache.commons.csv.CSVFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class CSVFormat_42_3Test {

    private CSVFormat csvFormat;
    private Method toStringArrayMethod;

    @BeforeEach
    public void setUp() throws Exception {
        csvFormat = CSVFormat.DEFAULT;
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_NullInput() throws Exception {
        Object[] input = null;
        // Pass null properly as varargs argument by passing null as the single argument (not new Object[]{null})
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNull(result, "Result should be null when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws Exception {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result, "Result should not be null");
        assertEquals(0, result.length, "Result length should be zero");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNonNullValues() throws Exception {
        Object[] input = new Object[]{"a", 123, 45.6, true, 'c'};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("a", result[0]);
        assertEquals("123", result[1]);
        assertEquals("45.6", result[2]);
        assertEquals("true", result[3]);
        assertEquals("c", result[4]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_WithNullValues() throws Exception {
        Object[] input = new Object[]{null, "test", null};
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertNull(result[0]);
        assertEquals("test", result[1]);
        assertNull(result[2]);
    }
}