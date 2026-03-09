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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CSVFormat_10_1Test {

    private void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNull() throws Exception {
        // Create a new CSVFormat instance with header = null using withHeader(null)
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        String[] result = format.getHeader();
        assertNull(result, "Expected getHeader() to return null when header is null");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderWhenHeaderIsNonNull() throws Exception {
        String[] headerValue = new String[] { "col1", "col2", "col3" };
        // Create a new CSVFormat instance with header = headerValue using withHeader(...)
        CSVFormat format = CSVFormat.DEFAULT.withHeader(headerValue);

        String[] result = format.getHeader();

        assertNotNull(result, "Expected getHeader() to return a non-null array");
        assertArrayEquals(headerValue, result, "Expected getHeader() to return a clone of header array");
        assertNotSame(headerValue, result, "Expected getHeader() to return a clone, not the original array");

        // Modify returned array and verify original is not affected
        result[0] = "modified";

        Field headerField = CSVFormat.class.getDeclaredField("header");
        headerField.setAccessible(true);
        String[] originalHeader = (String[]) headerField.get(format);
        assertEquals("col1", originalHeader[0], "Original header array should not be modified");
    }
}