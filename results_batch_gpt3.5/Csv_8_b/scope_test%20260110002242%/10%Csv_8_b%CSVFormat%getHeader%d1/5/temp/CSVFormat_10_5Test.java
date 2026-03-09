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

public class CSVFormat_10_5Test {

    private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
        Field field = CSVFormat.class.getDeclaredField(fieldName);
        field.setAccessible(true);

        // Remove final modifier from field (works on JDK 8 and below)
        Field modifiersField = Field.class.getDeclaredField("modifiers");
        modifiersField.setAccessible(true);
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(target, value);
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsCloneOfHeader() throws Exception {
        // Arrange
        String[] originalHeader = new String[]{"col1", "col2", "col3"};
        CSVFormat format = CSVFormat.DEFAULT.withHeader(originalHeader);

        // Use reflection to set the private final 'header' field, because withHeader returns a new instance
        setFinalField(format, "header", originalHeader);

        // Act
        String[] headerFromMethod = format.getHeader();

        // Assert
        assertNotNull(headerFromMethod, "Header should not be null");
        assertArrayEquals(originalHeader, headerFromMethod, "Header contents should match");
        assertNotSame(originalHeader, headerFromMethod, "Returned header array should be a clone, not the same instance");

        // Modify returned array and verify original is not changed
        headerFromMethod[0] = "modified";
        String[] headerAfterModification = format.getHeader();
        assertEquals("col1", headerAfterModification[0], "Original header array should not be affected by changes to returned array");
    }

    @Test
    @Timeout(8000)
    public void testGetHeaderReturnsNullWhenHeaderIsNull() throws Exception {
        // Arrange
        CSVFormat format = CSVFormat.DEFAULT.withHeader((String[]) null);

        // Use reflection to set the private final 'header' field to null
        setFinalField(format, "header", null);

        // Act
        String[] headerFromMethod = format.getHeader();

        // Assert
        assertNull(headerFromMethod, "Header should be null when internal header field is null");
    }
}