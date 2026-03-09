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
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_5Test {

    private Reader mockReader;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullReader_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(null, mockFormat);
        });
        assertEquals("reader", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            new CSVParser(mockReader, null);
        });
        assertEquals("format", ex.getMessage());
    }

    @Test
    @Timeout(8000)
    void testConstructor_callsFormatValidateAndInitializesHeaderMap() throws Exception {
        doNothing().when(mockFormat).validate();

        CSVParser parser = new CSVParser(mockReader, mockFormat);

        verify(mockFormat).validate();

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMap = (Map<String, Integer>) headerMapField.get(parser);
        assertNotNull(headerMap);

        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        CSVFormat formatFieldValue = (CSVFormat) formatField.get(parser);
        assertSame(mockFormat, formatFieldValue);

        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        Object lexer = lexerField.get(parser);
        assertNotNull(lexer);
    }

    @Test
    @Timeout(8000)
    void testConstructor_initializeHeaderCalled() throws Exception {
        doNothing().when(mockFormat).validate();

        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);
        CSVParser parser = constructor.newInstance(mockReader, mockFormat);

        Method initializeHeaderMethod = CSVParser.class.getDeclaredMethod("initializeHeader");
        initializeHeaderMethod.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMapFromMethod = (Map<String, Integer>) initializeHeaderMethod.invoke(parser);

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Map<String, Integer> headerMapFieldValue = (Map<String, Integer>) headerMapField.get(parser);

        assertEquals(headerMapFromMethod, headerMapFieldValue);
    }
}