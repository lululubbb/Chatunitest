package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import static org.apache.commons.csv.Token.Type.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CSVParser_4_3Test {

    private Reader mockReader;
    private CSVFormat mockFormat;

    @BeforeEach
    void setUp() {
        mockReader = mock(Reader.class);
        mockFormat = mock(CSVFormat.class);
    }

    @Test
    @Timeout(8000)
    void testConstructor_withValidArguments_initializesFields() throws Exception {
        // Mock Lexer instance
        Lexer mockLexer = mock(Lexer.class);

        // Use reflection to get the constructor with Reader and CSVFormat parameters
        Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
        constructor.setAccessible(true);

        // Create CSVParser instance
        CSVParser parser = constructor.newInstance(mockReader, mockFormat);

        // Inject mockLexer into parser's lexer field via reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, mockLexer);

        // Validate fields via reflection
        Field formatField = CSVParser.class.getDeclaredField("format");
        formatField.setAccessible(true);
        assertSame(mockFormat, formatField.get(parser));

        assertSame(mockLexer, lexerField.get(parser));

        Field headerMapField = CSVParser.class.getDeclaredField("headerMap");
        headerMapField.setAccessible(true);
        Map<?, ?> headerMap = (Map<?, ?>) headerMapField.get(parser);
        assertNotNull(headerMap);

        // recordNumber should be 0 by default
        Field recordNumberField = CSVParser.class.getDeclaredField("recordNumber");
        recordNumberField.setAccessible(true);
        assertEquals(0L, recordNumberField.getLong(parser));
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullReader_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            constructor.newInstance(null, mockFormat);
        });
        assertTrue(ex.getMessage().contains("reader"));
    }

    @Test
    @Timeout(8000)
    void testConstructor_nullFormat_throwsException() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            Constructor<CSVParser> constructor = CSVParser.class.getDeclaredConstructor(Reader.class, CSVFormat.class);
            constructor.setAccessible(true);
            constructor.newInstance(mockReader, null);
        });
        assertTrue(ex.getMessage().contains("format"));
    }
}