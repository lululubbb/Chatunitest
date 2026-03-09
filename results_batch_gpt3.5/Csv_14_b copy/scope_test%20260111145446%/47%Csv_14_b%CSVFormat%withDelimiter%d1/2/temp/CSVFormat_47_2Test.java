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

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyChar;
import static org.mockito.Mockito.doThrow;

public class CSVFormat_47_2Test {

    @Test
    @Timeout(8000)
    public void testWithDelimiter() {
        // Given
        char delimiter = ',';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat result = csvFormat.withDelimiter(delimiter);

        // Then
        assertEquals(delimiter, result.getDelimiter());
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_lineBreakDelimiter_exceptionThrown() {
        // Given
        char delimiter = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        try {
            csvFormat.withDelimiter(delimiter);
        } catch (IllegalArgumentException e) {
            // Then
            assertEquals("The delimiter cannot be a line break", e.getMessage());
        }
    }

    @Test
    @Timeout(8000)
    public void testWithDelimiter_usingMockito() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        // Given
        char delimiter = ',';
        Constructor<CSVFormat> constructor = CSVFormat.class.getDeclaredConstructor(char.class, Character.class, QuoteMode.class, Character.class, Character.class, boolean.class, boolean.class, String.class, String.class, Object[].class, String[].class, boolean.class, boolean.class, boolean.class, boolean.class, boolean.class);
        constructor.setAccessible(true);
        CSVFormat csvFormat = constructor.newInstance('|', '"', null, '#', '\\', true, false, "\r\n",
                "null", new Object[]{"header"}, new String[]{"header"}, true, false, true, false, true);

        Method isLineBreakMethod = CSVFormat.class.getDeclaredMethod("isLineBreak", char.class);
        isLineBreakMethod.setAccessible(true);
        doThrow(new IllegalArgumentException("Mocked exception")).when(isLineBreakMethod).invoke(csvFormat, anyChar());

        // When
        try {
            csvFormat.withDelimiter(delimiter);
        } catch (IllegalArgumentException e) {
            // Then
            assertEquals("Mocked exception", e.getMessage());
        }
    }
}