package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

class CSVParser_17_3Test {

    @Mock
    private Lexer mockLexer;

    private CSVParser csvParser;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        // Create CSVParser instance with a dummy Reader and CSVFormat
        csvParser = new CSVParser(new java.io.StringReader(""), CSVFormat.DEFAULT);

        // Inject the mock Lexer into the private final field 'lexer' using reflection
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(csvParser, mockLexer);
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(mockLexer.isClosed()).thenReturn(true);

        boolean result = csvParser.isClosed();

        assertTrue(result);
        verify(mockLexer).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(mockLexer.isClosed()).thenReturn(false);

        boolean result = csvParser.isClosed();

        assertFalse(result);
        verify(mockLexer).isClosed();
    }
}