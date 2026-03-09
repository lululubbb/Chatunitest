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

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class CSVFormat_49_3Test {

    @Test
    @Timeout(8000)
    public void testWithEscapeValid() {
        // Given
        Character escape = '|';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        CSVFormat newCsvFormat = csvFormat.withEscape(escape);

        // Then
        assertEquals(escape, newCsvFormat.getEscapeCharacter());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeInvalid() {
        // Given
        Character escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeInvalidUsingReflection() {
        // Given
        Character escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });

        // Then
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testWithEscapeInvalidUsingReflection2() throws NoSuchFieldException, IllegalAccessException {
        // Given
        Character escape = '\n';
        CSVFormat csvFormat = CSVFormat.DEFAULT;

        // When
        Field escapeField = CSVFormat.class.getDeclaredField("escapeCharacter");
        escapeField.setAccessible(true);
        escapeField.set(csvFormat, escape);

        // Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            csvFormat.withEscape(escape);
        });
        assertEquals("The escape character cannot be a line break", exception.getMessage());
    }
}