package org.apache.commons.csv;
import org.junit.jupiter.api.Timeout;
import static org.apache.commons.csv.Token.Type.TOKEN;
import java.io.Closeable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.Mockito.*;

import java.io.StringReader;
import java.lang.reflect.Field;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CSVParser_12_4Test {

    @Mock
    private Lexer lexerMock;

    private CSVParser csvParser;

    private AutoCloseable mocks;

    @BeforeEach
    void setUp() throws Exception {
        mocks = MockitoAnnotations.openMocks(this);
        // Use reflection to create CSVParser instance with mocked lexer
        csvParser = createCSVParserWithLexer(lexerMock);
    }

    @AfterEach
    void tearDown() throws Exception {
        if (mocks != null) {
            mocks.close();
        }
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsClosed_returnsTrue() {
        when(lexerMock.isClosed()).thenReturn(true);
        boolean result = csvParser.isClosed();
        assertTrue(result);
        verify(lexerMock).isClosed();
    }

    @Test
    @Timeout(8000)
    void testIsClosed_whenLexerIsNotClosed_returnsFalse() {
        when(lexerMock.isClosed()).thenReturn(false);
        boolean result = csvParser.isClosed();
        assertFalse(result);
        verify(lexerMock).isClosed();
    }

    // Helper method to create CSVParser instance with mocked Lexer
    private CSVParser createCSVParserWithLexer(Lexer lexer) throws Exception {
        // Create a dummy CSVFormat for constructor
        CSVFormat dummyFormat = mock(CSVFormat.class);
        // Create CSVParser instance using constructor
        CSVParser parser = new CSVParser(new StringReader(""), dummyFormat);

        // Use reflection to set private final lexer field to the mock
        Field lexerField = CSVParser.class.getDeclaredField("lexer");
        lexerField.setAccessible(true);
        lexerField.set(parser, lexer);

        return parser;
    }
}