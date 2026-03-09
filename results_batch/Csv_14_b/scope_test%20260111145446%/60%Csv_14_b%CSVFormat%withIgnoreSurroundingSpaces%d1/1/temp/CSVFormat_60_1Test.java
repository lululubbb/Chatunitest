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
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.StringWriter;

import org.junit.jupiter.api.Test;

public class CSVFormat_60_1Test {

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces();

        // Then
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    // Additional test cases for branch coverage

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_False() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT.withIgnoreSurroundingSpaces(false);

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces();

        // Then
        assertFalse(result.getIgnoreSurroundingSpaces());
    }

    @Test
    @Timeout(8000)
    public void testWithIgnoreSurroundingSpaces_Excel() {
        // Given
        CSVFormat csvFormat = CSVFormat.EXCEL;

        // When
        CSVFormat result = csvFormat.withIgnoreSurroundingSpaces();

        // Then
        assertTrue(result.getIgnoreSurroundingSpaces());
    }

    // Test using reflection to access private method for branch coverage

    @Test
    @Timeout(8000)
    public void testIsLineBreak() throws Exception {
        // Given
        char c = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        boolean result = invokePrivateMethod(csvFormat, "isLineBreak", char.class, c);

        // Then
        assertTrue(result);
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument)
            throws Exception {
        java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(object, argument);
    }
}