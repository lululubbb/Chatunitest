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

import java.lang.reflect.Method;

class CSVFormat_40_3Test {

    @Test
    @Timeout(8000)
    void testToStringArray_nullInput() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);
        // Pass null as the Object[] argument, so invoke with (csvFormat, (Object) null)
        Object result = toStringArray.invoke(csvFormat, new Object[] { null });
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_emptyArray() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);
        Object[] input = new Object[0];
        String[] result = (String[]) toStringArray.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(0, result.length);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_allNonNull() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);
        Object[] input = new Object[] { 1, "two", 3.0, true };
        String[] result = (String[]) toStringArray.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertEquals("1", result[0]);
        assertEquals("two", result[1]);
        assertEquals("3.0", result[2]);
        assertEquals("true", result[3]);
    }

    @Test
    @Timeout(8000)
    void testToStringArray_withNullElements() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        Method toStringArray = CSVFormat.class.getDeclaredMethod("toStringArray", Object[].class);
        toStringArray.setAccessible(true);
        Object[] input = new Object[] { null, "abc", null };
        String[] result = (String[]) toStringArray.invoke(csvFormat, new Object[] { input });
        assertNotNull(result);
        assertEquals(input.length, result.length);
        assertNull(result[0]);
        assertEquals("abc", result[1]);
        assertNull(result[2]);
    }
}