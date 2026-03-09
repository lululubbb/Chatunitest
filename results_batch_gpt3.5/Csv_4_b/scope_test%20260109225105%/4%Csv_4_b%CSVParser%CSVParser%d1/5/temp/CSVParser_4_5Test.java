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

class CSVParser_4_5Test {

    private CSVFormat mockFormat;
    private Reader validReader;

    @BeforeEach
    void setup() {
        mockFormat = mock(CSVFormat.class);
        validReader = new StringReader("header1,header2\nvalue1,value2");
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullReader_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertEquals("reader", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withNullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(validReader, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_callsFormatValidateAndInitializesFields() throws Exception {
        doNothing().when(mockFormat).validate();

        CSVParser parser = new CSVParser(validReader, mockFormat);

        verify(mockFormat).validate();

        // Using reflection to verify private final fields
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<?, ?> headerMap = (Map<?, ?>) headerMapField.get(parser);
        assertNotNull(headerMap);
    }

    @Test
    @Timeout(8000)
    void testConstructor_formatValidateThrowsIOException_propagates() throws Exception {
        doThrow(new IOException("validate failure")).when(mockFormat).validate();

        IOException ex = assertThrows(IOException.class, () -> {
            new CSVParser(validReader, mockFormat);
        });
        assertEquals("validate failure", ex.getMessage());
    }
}