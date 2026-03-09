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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_42_5Test {

    private Method toStringArrayMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        toStringArrayMethod = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArrayMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_NullInput() throws InvocationTargetException, IllegalAccessException {
        Object[] input = null;
        // Pass null as Object[] argument properly for varargs
        String[] result = (String[]) toStringArrayMethod.invoke(CSVFormat.DEFAULT, new Object[] { (Object) input });
        assertNull(result, "Expected null when input is null");
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNullElements() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {null, null};
        String[] result = (String[]) toStringArrayMethod.invoke(CSVFormat.DEFAULT, new Object[] { (Object) input });
        assertNotNull(result);
        assertEquals(2, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_MixedNullAndNonNull() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {null, 123, "abc", 45.6};
        String[] result = (String[]) toStringArrayMethod.invoke(CSVFormat.DEFAULT, new Object[] { (Object) input });
        assertNotNull(result);
        assertEquals(4, result.length);
        assertNull(result[0]);
        assertEquals("123", result[1]);
        assertEquals("abc", result[2]);
        assertEquals("45.6", result[3]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(CSVFormat.DEFAULT, new Object[] { (Object) input });
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_SingleElement() throws InvocationTargetException, IllegalAccessException {
        Object[] input = new Object[] {"single"};
        String[] result = (String[]) toStringArrayMethod.invoke(CSVFormat.DEFAULT, new Object[] { (Object) input });
        assertNotNull(result);
        assertEquals(1, result.length);
        assertEquals("single", result[0]);
    }
}