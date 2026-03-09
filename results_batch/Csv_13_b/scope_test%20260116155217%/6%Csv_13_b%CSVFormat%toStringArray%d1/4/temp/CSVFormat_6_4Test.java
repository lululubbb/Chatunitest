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
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class CSVFormat_6_4Test {

    @Test
    @Timeout(8000)
    void testToStringArray_nullInput() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        // Pass null directly, using varargs invocation style
        String[] result = (String[]) toStringArray.invoke(csvFormat, (Object) null);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_emptyArray() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[0];
        String[] result = (String[]) toStringArray.invoke(csvFormat, (Object) input);

        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_allNonNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[] { "abc", 123, 45.6, true, 'z' };
        String[] result = (String[]) toStringArray.invoke(csvFormat, (Object) input);

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
    void testToStringArray_someNulls() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);

        Object[] input = new Object[] { null, "test", null, 42 };
        String[] result = (String[]) toStringArray.invoke(csvFormat, (Object) input);

        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertNull(result[0]);
        assertEquals("test", result[1]);
        assertNull(result[2]);
        assertEquals("42", result[3]);
    }
}