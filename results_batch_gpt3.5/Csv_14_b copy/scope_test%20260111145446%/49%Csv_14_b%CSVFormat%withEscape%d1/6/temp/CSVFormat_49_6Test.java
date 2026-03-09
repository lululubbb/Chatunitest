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
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class CSVFormat_49_6Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValidCharacter() {
        // Given
        Character escape = '|';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withEscape(escape);

        // Then
        assertEquals(escape, result.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeInvalidCharacter() {
        // Given
        Character escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> csvFormat.withEscape(escape));

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapePrivateMethodInvocation() throws IOException {
        // Given
        Character escape = '|';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = invokePrivateMethod(csvFormat, "withEscape", Character.class, escape);

        // Then
        assertEquals(escape, result.getEscapeCharacter());
    }

    private <T> T invokePrivateMethod(Object object, String methodName, Class<?> parameterType, Object argument)
            throws IOException {
        try {
            java.lang.reflect.Method method = object.getClass().getDeclaredMethod(methodName, parameterType);
            method.setAccessible(true);
            return (T) method.invoke(object, argument);
        } catch (Exception e) {
            throw new IOException("Error invoking private method: " + methodName, e);
        }
    }
}