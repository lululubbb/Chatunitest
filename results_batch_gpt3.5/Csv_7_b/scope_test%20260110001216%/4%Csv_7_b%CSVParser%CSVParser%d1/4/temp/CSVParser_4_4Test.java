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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_4Test {

    private CSVFormat mockFormat;
    private Reader reader;

    @BeforeEach
    void setUp() {
        mockFormat = mock(CSVFormat.class);
        reader = new StringReader("header1,header2\nvalue1,value2");
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullReader_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertTrue(ex.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_NullFormat_ThrowsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(reader, null);
        });
        assertTrue(ex.getMessage().contains("format"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_ValidArguments_InitializesFields() throws Exception {
        // Arrange
        doNothing().when(mockFormat).validate();

        // Act
        CSVParser parser = new CSVParser(reader, mockFormat);

        // Assert
        // format field
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        // lexer field not null
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        assertNotNull(lexerField.get(parser));

        // headerMap initialized (not null)
        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);

        // recordNumber initialized to 0
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        long recordNumber = recordNumberField.getLong(parser);
        assertEquals(0L, recordNumber);

        // record list initialized and empty
        Field recordField = CSVParser.class.getDeclaredField("record");
        recordField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<?> recordList = (List<?>) recordField.get(parser);
        assertNotNull(recordList);
        assertTrue(recordList.isEmpty());

        // reusableToken field not null
        Field tokenField = CSVParser.class.getDeclaredField("reusableToken");
        tokenField.setAccessible(true);
        assertNotNull(tokenField.get(parser));
    }

    @Test
    @Timeout(8000)
    void testConstructor_FormatValidateThrowsException() throws IOException {
        doThrow(new IOException("validate failed")).when(mockFormat).validate();
        IOException ex = assertThrows(IOException.class, () -> new CSVParser(reader, mockFormat));
        assertEquals("validate failed", ex.getMessage());
    }
}