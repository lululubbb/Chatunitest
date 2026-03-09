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
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVFormat_6_3Test {

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
        // Pass null as the varargs argument by passing null explicitly
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, new Object[] { (Object) null });
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_EmptyArray() throws Exception {
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_AllNullElements() throws Exception {
        Object[] input = new Object[] { null, null };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertNull(result[0]);
        assertNull(result[1]);
    }

    @Test
    @Timeout(8000)
    public void testToStringArray_MixedElements() throws Exception {
        Object[] input = new Object[] { "test", 123, null, 45.6, true };
        String[] result = (String[]) toStringArrayMethod.invoke(csvFormat, (Object) input);
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("test", result[0]);
        assertEquals("123", result[1]);
        assertNull(result[2]);
        assertEquals("45.6", result[3]);
        assertEquals("true", result[4]);
    }
}