package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_3Test {

    private CSVFormat mockFormat;
    private Reader mockReader;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
        mockReader = mock(Reader.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_validParameters_initializesFields() throws Throwable {
        // Arrange
        doNothing().when(mockFormat).validate();

        // Use reflection to access private fields after construction
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Act
        CSVParser parser = null;
        try {
            parser = constructor.newInstance(mockReader, mockFormat);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }

        // Assert
        // Verify that format.validate() was called
        verify(mockFormat).validate();

        // Verify private field 'format' is assigned
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        // Verify private field 'lexer' is not null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        assertNotNull(lexerField.get(parser));

        // Verify private field 'headerMap' is not null
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        assertNotNull(headerMapField.get(parser));
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullReader_throwsException() throws Exception {
        // Arrange
        Reader nullReader = null;
        doNothing().when(mockFormat).validate();

        // Act & Assert
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            constructor.newInstance(nullReader, mockFormat);
        });

        // Check cause chain for NullPointerException with message containing "reader"
        Throwable cause = ex.getCause();
        boolean found = false;
        while (cause != null) {
            if (cause instanceof NullPointerException && cause.getMessage() != null && cause.getMessage().contains("reader")) {
                found = true;
                break;
            }
            cause = cause.getCause();
        }
        assertTrue(found, "Expected NullPointerException with message containing 'reader'");
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() throws Exception {
        // Arrange
        CSVFormat nullFormat = null;

        // Act & Assert
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            constructor.newInstance(mockReader, nullFormat);
        });

        // Check cause chain for NullPointerException with message containing "format"
        Throwable cause = ex.getCause();
        boolean found = false;
        while (cause != null) {
            if (cause instanceof NullPointerException && cause.getMessage() != null && cause.getMessage().contains("format")) {
                found = true;
                break;
            }
            cause = cause.getCause();
        }
        assertTrue(found, "Expected NullPointerException with message containing 'format'");
    }

    @Test
    @Timeout(8000)
    void testConstructor_formatValidateThrowsIOException_propagates() throws Exception {
        // Arrange
        doThrow(new IOException("validate failed")).when(mockFormat).validate();

        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Act & Assert
        InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> constructor.newInstance(mockReader, mockFormat));
        Throwable cause = ex.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IOException);
        assertEquals("validate failed", cause.getMessage());
    }
}