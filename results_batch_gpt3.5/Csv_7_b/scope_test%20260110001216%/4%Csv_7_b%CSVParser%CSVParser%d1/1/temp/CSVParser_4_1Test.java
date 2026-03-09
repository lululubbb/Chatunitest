package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class CSVParser_4_1Test {

    private CSVFormat mockFormat;
    private Reader mockReader;

    @BeforeEach
    public void setUp() {
        mockFormat = mock(CSVFormat.class);
        mockReader = new StringReader("header1,header2\nvalue1,value2\n");
    }

    @Test
    @Timeout(8000)
    public void testConstructor_validInput_initializesFields() throws Exception {
        // Arrange
        doNothing().when(mockFormat).validate();
        when(mockFormat.getHeader()).thenReturn(new String[] { "header1", "header2" });

        // Act
        CSVParser parser = new CSVParser(mockReader, mockFormat);

        // Assert
        assertNotNull(parser);

        // Validate format field
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatValue = (CSVFormat) formatField.get(parser);
        assertSame(mockFormat, formatValue);

        // Validate headerMap field is not null and contains expected keys
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);
        assertTrue(headerMap.containsKey("header1"));
        assertTrue(headerMap.containsKey("header2"));

        // Validate lexer field is not null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullReader_throwsException() {
        // Arrange
        doNothing().when(mockFormat).validate();

        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertTrue(exception.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_nullFormat_throwsException() {
        // Act & Assert
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            new CSVParser(mockReader, null);
        });
        assertTrue(exception.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_formatValidateThrowsException_propagates() throws IOException {
        // Arrange
        doThrow(new IOException("format invalid")).when(mockFormat).validate();

        // Act & Assert
        IOException thrown = assertThrows(IOException.class, () -> {
            new CSVParser(mockReader, mockFormat);
        });
        assertEquals("format invalid", thrown.getMessage());
    }

}