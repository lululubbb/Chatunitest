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
import static org.mockito.Mockito.*;

import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class CSVFormat_72_4Test {

    @Test
    @Timeout(8000)
    public void testWithTrim() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withTrim();

        // Then
        assertTrue(result.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testWithTrimFalse() {
        // Given
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withTrim(false);

        // Then
        assertFalse(result.getTrim());
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakChar() throws Exception {
        // Given
        char c = '\n';

        // When
        boolean result = invokePrivateStaticMethod(CSVFormat.class, "isLineBreak", char.class, c);

        // Then
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    public void testIsLineBreakCharacter() throws Exception {
        // Given
        Character c = '\r';

        // When
        boolean result = invokePrivateStaticMethod(CSVFormat.class, "isLineBreak", Character.class, c);

        // Then
        assertTrue(result);
    }

    // Helper method to invoke private static method using reflection
    private <T> T invokePrivateStaticMethod(Class<?> clazz, String methodName, Class<?> parameterType, Object argument)
            throws Exception {
        java.lang.reflect.Method method = clazz.getDeclaredMethod(methodName, parameterType);
        method.setAccessible(true);
        return (T) method.invoke(null, argument);
    }
}